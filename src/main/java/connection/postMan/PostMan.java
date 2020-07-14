package connection.postMan;

import connection.interfaces.IncomeObserver;
import connection.messages.Message;
import connection.messages.MessageStatus;
import connection.serial.SerialBufferedAdapter;
import exception.PortNotFoundException;
import exception.PortNotOpenException;
import utils.ThreadPool;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Class managing the input and output of a <code>SerialPort</code> sending
 * and receiving <code>Messages</code>.
 * </p>
 * <p>
 * It has a list of <code>IncomeObserver</code> to be notified whenever a
 * new answer arrives.
 * </p>
 */
public class PostMan extends SerialBufferedAdapter {

    /**
     * List of <code>IncomeObserver</code> to be notified.
     */
    private List<IncomeObserver> observerList = new ArrayList<>(4);

    /**
     * The queue of <code>Message</code> to be sent.
     */
    private MessageQueue queue = new MessageQueue();

    /**
     * <code>Message</code> that is being sending.
     */
    private Message actualMessage;

    private boolean inUse = false;

    private final Object lock = new Object();

    private final Object toObject = new Object();

    // TODO: 13/07/20 comment it
    protected PostMan(String portName, int baudRate) throws PortNotOpenException, PortNotFoundException {
        super(portName, baudRate);
    }

    // TODO: 13/07/20 comment it
    public PostMan(String portName) throws PortNotFoundException, PortNotOpenException {
        super (portName);
    }

    /**
     * Method to add an <code>IncomeObserver</code> to the list
     * of observers to notify.
     *
     * @param observer (the <code>IncomeObserver</code> to be added).
     */
    public void addListener(IncomeObserver observer){
        observerList.add(observer);
    }

    /**
     * Method to remove an <code>IncomeObserver</code> from the list
     * of observers to notify.
     *
     * @param observer (the <code>IncomeObserver</code> to be removed).
     */
    public void removeListener(IncomeObserver observer){
        observerList.remove(observer);
    }

    /**
     * Method to add a <code>Message</code> to the queue of the ones
     * to be sent.
     *
     * @param message (the <code>Message</code> to be sent).
     */
    public void addMessage(Message message){
        queue.add(message);
        // TODO: 13/07/20 call a thread to execute it. 
    }

    /**
     * Method to retrieve the <code>inUse</code> in a synchronised
     * way.
     *
     * @return (<code>inUse</code>).
     */
    protected boolean getInUse(){
        synchronized (lock){
            return inUse;
        }
    }

    /**
     * Method to set the <code>inUse</code> in a synchronised
     * way.
     *
     * @param inUse (the <code>inUse</code> to be set).
     */
    protected void setInUse(boolean inUse){
        synchronized (lock){
            this.inUse = inUse;
        }
    }

    /**
     * Method to set inUse to true and check if it already was or not.
     * it returns false iif inUse was previously already set to true.
     *
     * @return (false iif inUse was previously already set to true).
     */
    protected boolean setInUse(){
        synchronized (lock){
            if(inUse)
                return false;
            inUse = true;
            return true;
        }
    }

    /**
     * Method to set inUse to false and check if it already was or not.
     * it returns false iif inUse was previously already set to false.
     *
     * @return (false iif inUse was previously already set to false).
     */
    protected boolean setUnUsed(){
        synchronized (lock){
            if(!inUse)
                return false;
            inUse = false;
            return true;
        }
    }

    /**
     * Method to set <code>actualMessage</code>.
     *
     * @param message
     */
    protected void setActualMessage(Message message){
        this.actualMessage=message;
        actualMessage.setStatus(MessageStatus.LOADED);
    }

    /**
     * Method to send the next <code>Message</code> enqueued
     * ont the <code>Queue</code>.
     *
     * @throws PortNotOpenException (if message cannot be sent).
     */
    protected void sendNextMessage() {
        if(actualMessage != null)
            //cannot sent the message
            return;

        //Retrieve the message
        if(queue.available())
            this.setActualMessage(queue.get());
        else
            return;

        //Send the message
        try {
            super.sendMsg(actualMessage.getPayload());
        } catch (PortNotOpenException e) {
            actualMessage.killing();
            notifyException(e);
        }

        //checks for the timeout
        new Thread(){
            //saves a copy of message
            Message message = actualMessage;
            @Override
            public void run() {
                super.run();
                try {
                    //waits the timeout
                    toObject.wait(message.getTimeOut());
                } catch (InterruptedException ignore) {
                }
                //checks if the message hasn't been answered
                if(message.getStatus() == MessageStatus.SENT){
                    //kills the message
                    message.killing();

                    //set the message to be sent to null.
                    if(message==actualMessage) {
                        setActualMessage(null);
                        callAThread();
                    }
                }
            }
        }.start();
    }

    /**
     * <p>
     * Method to notify all the <code>IncomeObserver</code> registered
     * that a new <code>Answer</code> arrived.
     * </p><p>
     * It calls all the <code>IncomeObserver</code> method to notify the
     * newly arrived <code>Message</code>telling even if the arrived is
     * a <code>Answer</code> to previously sent <code>Message</code> or
     * not.
     * </p>
     *
     * @param answer (the <code>Answer</code> arrived as a <code>String</code>).
     */
    protected void notifyAnswer(String answer){
        boolean actMessage = false;

        if(actualMessage!=null){
            actMessage = true;
            actualMessage.evaluateAnswer(answer);
            setActualMessage(null);
            synchronized (toObject){
                toObject.notifyAll();
            }
        }

        for(IncomeObserver observer: observerList){
            observer.incomeMessage(answer, actMessage);
        }

        callAThread();
    }

    protected void notifyException(Exception exception){
        for(IncomeObserver observer: observerList)
            observer.exceptionThrown(exception);
    }

    /**
     * Method to call a thread (from the <code>ThreadPool</code>) to
     * send the next <code>Message</code> that has to be sent.
     */
    private void callAThread(){
        synchronized (lock){
            if(actualMessage != null)
                return;
            ThreadPool.safeSubmit(this::sendNextMessage);
        }
    }

    /**
     * <p>
     * Overridden method called when a message arrives. The message is passed
     * as a <code>String</code> as a parameter.
     * </p><p>
     * It'll notify to all the observers -on another thread- and notify it as
     * an answer if it is needed.
     * </p>
     *
     * @param message (the received complete message as a <code>String</code>
     */
    @Override
    protected void receivedMessage(String message) {
        ThreadPool.safeSubmit(() -> notifyAnswer(message));
    }
}
