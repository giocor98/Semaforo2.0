package connection.messages;

/**
 * <p>Enum representing the status of a message.</p>
 * <p>
 * A normal flow of state is: <br/>
 * READY (on the creation of <code>Message</code>) <br/>
 * LOADED (just before the sending of <code>Message</code>) <br/>
 * SENT (after the sending and before the receivement of the response to the <code>Message</code>)<br/>
 * RECEIVED | ACKED | ERROR (after the <code>Message</code> has been sent).
 * </p>
 */
public enum MessageStatus {
    /**
     * Message enqueued.
     */
    READY,
    /**
     * Message ready to be sent.
     */
    LOADED,
    /**
     * Message sent.
     */
    SENT,
    /**
     * Message received from Arduino.
     */
    RECEIVED,
    /**
     * Message acked.
     */
    ACKED,
    /**
     * Message errored back.
     */
    ERROR;
}
