package src.utils.connection;

import src.app.MainApp;
import src.utils.connection.interfaces.IncomeObserver;
import src.utils.connection.messages.Message;
import src.utils.connection.postMan.PostMan;
import src.utils.connection.serial.SerialManager;
import src.utils.exception.PortNotFoundException;
import src.utils.exception.PortNotOpenException;

import java.util.List;

/**
 * <p>
 * Facade class to interface with the src.utils.connection package.
 * </p>
 */
public class Connection {

    /**
     * the <code>PostMan</code> managing this.
     *
     * @see PostMan
     */
    private PostMan postMan;

    /**
     * Reference to the main application running this.
     *
     * @see MainApp
     */
    private MainApp app;

    /**
     *
     * Static method that returns the list of the name of all the
     * available SerialPort.
     *
     * @return (List of the name of the available <code>SerialPort</code>).
     * @see SerialManager#availablePorts()
     */
    public static List<String> availablePorts(){
        return SerialManager.availablePorts();
    }

    /**
     * <p>
     * Factory method not throwing Exceptions.
     * </p><p>
     * It'll return either or the newly created <code>Connection</code> or
     * <code>null</code> if the <code>Connection</code> cannot be created.
     * </p><p>
     * It must be checked if it returns <code>null</code>.
     * </p><p>
     * As parameter it takes the <code>String</code> name of the port to be
     * opened.
     * </p>
     *
     * @param portName (the <code>String</code> name of the port to be opened).
     * @return         (the newly created <code>Connection</code> or <code>null</code>
     *                 if any problem occurs).
     * @see #Connection(String)
     */
    public static Connection build(String portName){
        try {
            return new Connection(portName);
        } catch (PortNotOpenException | PortNotFoundException e) {
            return null;
        }
    }

    /**
     * Static method to close all the <code>SerialManager</code> opened.
     *
     * @see SerialManager#closeAll()
     */
    public static void closeAll(){
        SerialManager.closeAll();
    }

    /**
     * <p>
     * Constructor throwing exceptions.
     * </p><p>
     * It returns the desired <code>Connection</code> for the selected
     * <code>SerialPort</code>, selecting it by the parameter indicating
     * its name.
     * </p>
     *
     *
     * @param portName (the <code>String</code> indicating the <code>SerialPort</code> name).
     * @throws PortNotFoundException  (iif there is no <code>SerialPort</code>
     *                                 available with such a port name).
     * @throws PortNotOpenException   (iif the selected port exists but cannot
     *                                be accessed).
     * @see PostMan#PostMan(String, src.utils.threadPool.ThreadPool)
     */
    public Connection(String portName) throws PortNotOpenException, PortNotFoundException {
        postMan = new PostMan(portName, app.getThreadPool());
    }

    /**
     * <p>
     * Method to add a <code>IncomeObserver</code> as a listener to this'<code>PostMan</code>.
     * </p>
     *
     * @param observer (the <code>IncomeObserver</code> to be added to this <code>PostMan</code>).
     * @see PostMan#addListener(IncomeObserver)
     */
    public void addListener(IncomeObserver observer){
        postMan.addListener(observer);
    }

    /**
     * <p>
     * Method to remove a <code>IncomeObserver</code> from the listeners to this'<code>PostMan</code>.
     * </p>
     *
     * @param observer (the <code>IncomeObserver</code> to be removed from this <code>PostMan</code>).
     * @see PostMan#removeListener(IncomeObserver)
     */
    public void removeListener(IncomeObserver observer){
        postMan.removeListener(observer);
    }

    /**
     * Method to send a <code>Message</code> through this'<code>PostMan</code>.
     *
     * @param message (the <code>Message</code> to be sent).
     * @see PostMan#addMessage(Message)
     */
    public void sendMessage(Message message){
        postMan.addMessage(message);
    }
}
