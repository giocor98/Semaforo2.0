package connection.messages;

import connection.interfaces.AnswerEvaluator;
import connection.interfaces.DefaultEvaluator;

/**
 * Class to represent the <code>Message</code>.
 */
public class Message {

    /**
     * The <code>Message</code> payload.
     */
    private String payload;

    /**
     * The <code>MessageStatus</code> to represent the status of this.
     *
     * @see MessageStatus
     */
    private MessageStatus status;

    /**
     * The timeout from the sending of the message after which this
     * must be replied by the Arduino.
     * <p>
     * default set to 1000
     * </p>
     */
    private long timeOut = 1000;

    /**
     * <p>Boolean indicating weather there are some operations performing
     * on this or not.</p>
     * <p>This is the Object on which the synchronisation for this is kept.</p>
     */
    private Boolean inUse = false;

    private final Object lock = new Object();

    /**
     * <p>The <code>AnswerEvaluator</code> relative to this.</p>
     * <p>There should be only one <code>AnswerEvaluator</code>
     * for each <code>Message</code>, and if not set it'll be the
     * default one.</p>
     *
     * @see AnswerEvaluator
     */
    private AnswerEvaluator evaluator;

    /**
     * Constructor with payload.
     *
     * @param payload (the <code>String</code> payload).
     */
    Message(String payload){
        //Sets the payload
        this.payload = payload;
        //Sets the status
        this.status = MessageStatus.READY;
    }


    /**
     * Method to retrieve the set timeOut.
     *
     * @return (this' timeOut).
     * @see #timeOut
     */
    public long getTimeOut() {
        return timeOut;
    }

    /**
     * <p>Method to set this timeOut IF possible.</p>
     * <p>
     * the timeout can be set iif <code>this.status</code>
     * is <code>READY</code>, else it means this is in use.
     * It's not send back if the change has been done or not.
     * </p>
     *
     * @param timeOut (the new <code>timeout</code> to be set).
     * @see #timeOut
     */
    public void setTimeOut(long timeOut) {
        if (!this.isInUse()&&this.getStatus().equals(MessageStatus.READY))
            this.timeOut = timeOut;
    }

    /**
     * <p>Method to retrieve <code>inUse</code> in a synchronised way.</p>
     *
     * @return (true iif there is someone using this).
     * @see #inUse
     */
    public boolean isInUse(){
        synchronized (lock){
            return inUse;
        }
    }


    /**
     * <p>Method to retrieve this' status</p>
     *
     * @return (this'status).
     * @see #status
     */
    public MessageStatus getStatus() {
        return status;
    }

    /**
     * <p>Method to set this'satus.</p>
     * <p>It's possible to set this'status iif <code>this.innUse()</code>
     * is true, so if the caller is performing changes to this.</p>
     * <p>WARNING there is no control on who is performing the changes,
     * so it's a caller responsibility to assure it's the one performing
     * changes to this.</p>
     * <p>It returns true iif at the end of the operation the status is
     * the one the caller wanted to be set, even if there hasn't been
     * performed any operation</p>
     *
     * @param status (the new status to be set).
     * @return       (true iif at the end <code>this.status</code> is
     *                the same <code>MessageStatus</code> passed as
     *                parameter).
     * @see #status
     * @see #inUse
     */
    public boolean setStatus(MessageStatus status) {
        //check if in use
        if (this.isInUse())
            this.status = status;
        return this.status==status;
    }

    /**
     * <p>Method to atomically set this'satus.</p>
     * <p>
     * It should be called to change the status with an
     * atomic operation WITHOUT having this in use.
     * It is autonomous for the switching of the various
     * <code>inUse</code> phases.
     * </p>
     * <p>
     * I returns true iif the set has been done, else
     * if this was previously in use, it'll return false.
     * </p>
     *
     * @param status (the <code>MessageStatus</code> to be set).
     * @return (true if the status has been changed and false if
     *         this was previously in use).
     */
    public boolean atomicSetStatus(MessageStatus status){
        if(!this.setInUse())
            return false;
        this.setStatus(status);
        this.release();
        return true;
    }

    /**
     * <p>Method to set this in use.</p>
     * <p>It should be called before doing any action
     * on this. After the operations have been performed
     * call <code>release</code>.</p>
     *
     * @return (true  iif it has been set in use with the
     *          call, else -if previously in use- returns
     *          false. It shouldn't be done any action if
     *          it returns false).
     */
    public boolean setInUse(){
        synchronized (lock){
            if (inUse)
                return false;
            inUse = true;
            return true;
        }
    }

    /**
     * <p>Method to release this. It sets <code>this.inUse</code>
     * to false in a synchronised way.</p>
     * <p>It's recommended to call this method after the <code>
     * setInUse</code> one.</p>
     */
    public void release(){
        synchronized (lock){
            inUse = false;
        }
    }

    /**
     * <p>
     * Method to retrieve the <code>AnswerEvaluator</code> used for this.
     * </p>
     * <p>
     * If <code>this.status</code> is READY, then it shouldn't be needed
     * the <code>AnswerEvaluator</code>, so it'll return null.
     * </p>
     * <p>
     * If it should return the evaluator but hasn't be set so far, it'll set
     * it to the <code>DefaultEvaluator</code> and return it. Once the
     * <code>evaluator</code> has been set it cannot be changed.
     * </p>
     *
     * @return (the <code>AnswerEvaluator</code> needed for this or null
     *          if <code>this.status</code> is READY).
     */
    public AnswerEvaluator getEvaluator(){
        if(getStatus().equals(MessageStatus.READY))
            return null;
        if(evaluator==null)
            evaluator = new DefaultEvaluator();
        return evaluator;
    }

    /**
     * <p>Method to set the <code>evaluator</code>.</p>
     * <p>
     * It sets the evaluator iif it hasn't been already
     * set, either with previous call to that method or
     * with call to <code>getEvaluator()</code>.
     * </p>
     *
     * @param evaluator (the new <code>AnswerEvaluator</code>
     *                  to be set for this).
     */
    public void setEvaluator(AnswerEvaluator evaluator){
        if(this.evaluator==null)
            this.evaluator = evaluator;
    }

    /**
     * <p>Method to be called on the arrival of an answer to this.</p>
     * <p>
     * It calls the right <code>AnswerEvaluator</code> and sets the right
     * <code>status</code> after the call.
     * </p>
     * <p>
     * If the <code>this.status</code> is READY, it should throw a null
     * pointer exception (and also in other cases).
     * </p>
     *
     * @param answer (the <code>String</code> of the answer received).
     * @throws NullPointerException (if <code>this.status</code> is READY).
     */
    public void evaluateAnswer(String answer) throws NullPointerException{
        this.setStatus(this.getEvaluator().evaluate(answer));
    }

    /**
     * Method called to notify to call the <code>killing()</code> method on the
     * <code>AnswerEvaluator</code> related to this.
     *
     * @throws NullPointerException (if the evaluator is not valid).
     */
    public void killing() throws NullPointerException{
        this.getEvaluator().killing();
    }

    public String getPayload() {
        return payload;
    }
}
