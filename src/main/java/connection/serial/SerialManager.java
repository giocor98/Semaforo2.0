package connection.serial;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import exception.PortNotFoundException;
import exception.PortNotOpenException;

/**
 * <p>Class to manage a Serial port.</p>
 * <p>It allows to perform:
 * OPENING[ {@link #SerialManager(String, int, SerialPortDataListener) constructor}, {@link #SerialManager(String, SerialPortDataListener) constructor} ],
 * WRITING[ {@link #sendMsg(String) send String}, {@link #sendBytes(byte[], long) send bytes}, {@link #sendBytes(byte[]) send bytes} ],
 * READING (setting a listener on the port) [ {@link #addListener(SerialPortDataListener) add a listener} ],
 * CLOSING[ {@link #close() close this} ]
 * operation on a serial port.</p>
 *
 * <p>
 * It also offers a method to close all the currently opened
 * SerialPorts (to guarantee a right way to exit the program).
 * [ {@link #closeAll() close all} ]
 * </p>
 * <p>
 * It gives the possibility to retrieve the list of available
 * ports.
 * </p>
 */
public class SerialManager {

    /**
     * List of all the opened ports, so it'll be easier to notify the
     * closure to each of them.
     */
    static private List<SerialManager> portsList = new ArrayList<>(3);

    /**
     * The actual <code>SerialPort</code> that is managed by this.
     *
     * @see SerialPort
     */
    private SerialPort port;

    /**
     *
     * Static method that returns the list of the name of all the
     * available SerialPort.
     *
     * @return (List of the name of the available <code>SerialPort</code>).
     */
    static List<String> availablePorts(){

        //retrieving the array of all the available ports
        SerialPort[] sp = SerialPort.getCommPorts();

        //building the list of available ports and returning it.
        //it uses the functional java and does:
        // Start from the array of available ports;
        // Converts it into a Stream;
        // map each port into its name;
        // converts the stream into a list.
        return Arrays.stream(sp).map(SerialPort::getSystemPortName).collect(Collectors.toList());
    }

    /**
     * CLOSING:
     * Method closing all the opened <code>SerialManager</code>.
     */
    static void closeAll(){
        //Iterate on the array of the ports and close them.
        for (SerialManager sm: portsList) sm.close();
    }

    /**
     * OPENING:
     * <p>
     * Constructor. It permits to select the <code>SerialPort</code>
     * with the portName parameter, the baud rate can be chosen with
     * the baudRate parameter, and there is also the possibility to
     * set a <code>SerialPortDataListener</code> on the selected port.
     * </p>
     * <p>
     * Once this has been opened, it adds the <code>SerialManager</code>
     * to the queue of the opened ones.
     * </p>
     *
     * @param portName (the <code>String</code> with the name of the port).
     * @param baudRate (the <code>int</code> representing the baud rate).
     * @param serialPortDataListener  (the <code>SerialPortDataListener</code>
     *                                to be put on the <code>SerialPort</code>).
     * @throws PortNotFoundException  (iif there is no <code>SerialPort</code>
     *                                 available with such a port name).
     * @throws PortNotOpenException   (iif the selected port exists but cannot
     *                                be accessed).
     *
     * @see SerialPortDataListener
     * @see PortNotFoundException
     * @see PortNotOpenException
     */
    SerialManager(String portName, int baudRate, SerialPortDataListener serialPortDataListener) throws PortNotFoundException, PortNotOpenException {

        //check if the given portName is valid and sets port to the right value.
        port = null;
        SerialPort[] tmp = SerialPort.getCommPorts();
        for (SerialPort i: tmp){
            if(portName.equals(i.getSystemPortName())){
                port=i;
                break;
            }
        }
        //If the portName is not a valid name
        if (port==null){
            //throws PortNotFoundException
            throw new PortNotFoundException("SerialData constructor: port not found");
        }

        //Sets the baudrate
        port.setBaudRate(baudRate);

        //If the port is not opened
        if(!port.openPort()){
            //Sets port to null... It should be useless, BTW...
            port=null;
            //throws PortNotOpenException
            throw new PortNotOpenException("SerialData constructor: cannot open the port");
        }

        //if listener != null adds the listener to the port.
        if(serialPortDataListener != null) port.addDataListener(serialPortDataListener);

        //enqueue the port just created.
        portsList.add(this);
    }

    /**
     * OPENING:
     * <p>
     * {@link #SerialManager(String, int, SerialPortDataListener) constructor} overloaded. It works as the default constructor
     * but automatically sets the baud rate value to 115200, the
     * default value.
     * </p>
     * <p>
     * It permits to select the <code>SerialPort</code>
     * with the portName parameter, and there is the possibility to
     * set a <code>SerialPortDataListener</code> on the selected port.
     * </p>
     * <p>
     * Once this has been opened, it adds the <code>SerialManager</code>
     * to the queue of the opened ones.
     * </p>
     *
     * @param portName (the <code>String</code> with the name of the port).
     * @param serialPortDataListener  (the <code>SerialPortDataListener</code>
     *                                to be put on the <code>SerialPort</code>).
     * @throws PortNotFoundException  (iif there is no <code>SerialPort</code>
     *                                 available with such a port name).
     * @throws PortNotOpenException   (iif the selected port exists but cannot
     *                                be accessed).
     * @see #SerialManager(String, SerialPortDataListener)
     * @see SerialPortDataListener
     * @see PortNotFoundException
     * @see PortNotOpenException
     */
    SerialManager(String portName, SerialPortDataListener serialPortDataListener) throws PortNotFoundException, PortNotOpenException {
        this(portName, 115200, serialPortDataListener);
    }

    /**
     * WRITING:
     * <p>
     * Method to write an array of bytes on this <code>SerialPort</code>.
     * </p>
     * <p>
     * WARNING: the port MUST be opened, it'll not be checked in this method!
     * </p>
     *
     * @param payload      (the array of bytes to be sent onto the Port).
     * @param numToWrite   (the number of bytes of the array to be sent).
     */
    private void sendBytes(byte[] payload, long numToWrite){
        port.writeBytes(payload, numToWrite);
    }

    /**
     * WRITING:
     * <p>
     * Overwritten {@link #sendBytes(byte[], long) sendBytes} without the
     * number of bytes to write (it retrieves it from the payload.length).
     * </p>
     * <p>
     * WARNING: the port MUST be opened, it'll not be checked in this method!
     * </p>
     *
     * @param payload      (the array of bytes to be sent onto the Port).
     */
    private void sendBytes(byte[] payload){
        this.sendBytes(payload, payload.length);
    }

    /**
     * WRITING:
     * <p>
     * Method to send a message -codified as a <code>String</code> on this
     * <code>SerialPort</code>.
     * It also checks weather the <code>port</code> is opened or not.
     * </p>
     *
     * @param str (the <code>String</code> message to be sent).
     * @throws PortNotOpenException (iif this <code>port</code> is not opened).
     */
    void sendMsg(String str) throws PortNotOpenException {
        if(port == null)
            throw new PortNotOpenException("SerialData.sendMsg : stai cercando di inviare dati su di una porta chiusa");
        byte[] b = str.getBytes();
        sendBytes(b);
    }

    /**
     * CLOSING:
     * <p>
     * Method to close this <code>SerialManager</code>.
     * It closes the port and release it.
     * </p>
     */
    void close(){
        //Closes the port and loose its reference.
        port.closePort();
        port = null;
    }

    /**
     * READING:
     * <p>
     * Method to add a listener to this.
     * </p>
     *
     * @param serialPortDataListener (the listener to be added).
     * @throws PortNotOpenException  (iif the port is closed).
     *
     * @see SerialPortDataListener
     * @see PortNotOpenException
     */
    void addListener(SerialPortDataListener serialPortDataListener) throws PortNotOpenException{
        if(port==null) throw new PortNotOpenException("SerialManager.addListener : Trying to add a listener to a closed port");
        if(serialPortDataListener!=null) port.addDataListener(serialPortDataListener);
    }

    /**
     * Method that returns this' <code>SerialPort</code>.
     *
     * @return (this' <code>SerialPort</code>).
     */
    SerialPort getPort(){
        return port;
    }
}
