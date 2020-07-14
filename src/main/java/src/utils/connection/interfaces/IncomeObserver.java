package src.utils.connection.interfaces;

/**
 * Interface for all the Class to be notified on the arrival of an incoming <code>Message</code>
 * from the Arduino.
 */
public interface IncomeObserver {

    /**
     * Method called on the arrival of an incoming message.
     *
     * @param message (the <code>String</code> message arrived).
     * @param isAnAnswer (true iif the call is due to an answer to a previous message).
     */
    public void incomeMessage(String message, boolean isAnAnswer);

    /**
     * Method ccalled on an src.utils.exception of the <code>SerialBufferedAdapter</code>.
     *
     * @param exc (the <code>Exception thrown</code>).
     */
    public void exceptionThrown(Exception exc);
}
