package connection.serial;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import exception.PortNotFoundException;
import exception.PortNotOpenException;

/**
 * <p>
 * Abstract class that permits to send and retrieve messages to and from
 * the <code>SerialPort</code> in an simplified way.
 * </p>
 * <p>
 * It permits to manage the serial connection in a easy way, making it
 * possible to see the messages as String and abstracting from the actual
 * way the messages are sent and received. A message is received iif it
 * terminates with a '\n' character, and so this class sends a message
 * only if it is complete.
 * </p>
 * <p>
 * This extends the <code>SerialPortDataListener</code> to receive the
 * messages, and uses the <code>SerialManager</code> to deal with the
 * <code>SerialPort</code>
 * </p>
 * <p>
 * When a message is received this calls the method
 * {@link #receivedMessage(String) recivedMessage} that must be implemented
 * and that deals with the message received.
 * </p>
 *
 * @see SerialPortDataListener
 * @see SerialManager
 */
public abstract class SerialBufferedAdapter implements SerialPortDataListener {

    /**
     * The <code>SerialManager</code> managing the <code>SerialPort</code>
     */
    private SerialManager serialManager;
    /**
     * byte array working as a buffer for the input.
     */
    private byte[] buff;
    /**
     * index pointing to the top of the buff.
     */
    private int index;

    // TODO: 13/07/20 comment it 
    protected SerialBufferedAdapter(String portName, int baudRate) throws PortNotOpenException, PortNotFoundException {
        serialManager = new SerialManager(portName, baudRate, this);
    }

    // TODO: 13/07/20 comment it 
    protected SerialBufferedAdapter(String portName) throws PortNotOpenException, PortNotFoundException {
        serialManager = new SerialManager(portName, this);
    }

    /**
     * Method setting the listener only for available bytes.
     *
     * @return ( SerialPort.LISTENING_EVENT_DATA_AVAILABLE).
     * @see SerialPortDataListener#getListeningEvents()
     */
    @Override
    public int getListeningEvents() {
        return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
    }

    @Override
    public void serialEvent(SerialPortEvent serialPortEvent) {
        int prevIndex = index;

        //retrieving the SerialPort
        SerialPort serialPort = serialManager.getPort();

        //reading the message
        index = index + serialPort.readBytes(buff, buff.length-index, index);

        //checks if the message is complete or not
        for(int i= prevIndex; i<index; i++){
            if(buff[i] == (byte)'\n'){
                String received = new String(buff).substring(0,i+1);
                receivedMessage(received);
                for(int j = i+1; j<index; j++){
                    buff[j-1-i] = buff[j];
                }
                index = index - i - 1;
                i=-1;
            }
        }
    }

    /**
     * Method to retrieve the <code>SeriaalManager</code>
     * of this.
     *
     * @return (the <code>SerialManager</code> of this).
     */
    public SerialManager getSerialManager(){
        return serialManager;
    }

    /**
     * Method that sends the given message (as <code>String</code>)
     * through the this' <code>SerialManager</code>.
     *
     * @param str (the <code>String</code> message to be sent).
     * @throws PortNotOpenException (iif this <code>port</code> is not opened).
     *
     * @see SerialManager#sendMsg(String)
     */
    public void sendMsg(String str) throws PortNotOpenException{
        serialManager.sendMsg(str);
    }

    /**
     * <p>
     * Abstract method called when a message arrives. The message is passed
     * as a <code>String</code> as a parameter.
     * </p>
     *
     * @param message (the received complete message as a <code>String</code>
     */
    protected abstract void receivedMessage(String message);
}
