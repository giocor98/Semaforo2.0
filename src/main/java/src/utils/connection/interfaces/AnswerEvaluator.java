package src.utils.connection.interfaces;

import src.utils.connection.messages.MessageStatus;

/**
 * Interface used to implement the decorator pattern to manage the answers
 * from the Arduino and to guide the <code>Message</code> through a consistent
 * consecutio of valid <code>MessageStatus</code>.
 *
 * @see src.utils.connection.messages.MessageStatus
 * @see src.utils.connection.messages.Message
 */
public interface AnswerEvaluator {

    /**
     * Method called on the arrival of an answer for the evaluation of it.
     *
     * @param answer (the <code>String</code> answer received).
     * @return       (the next <code>MessageStatus</code> to which the
     *               <code>Message</code> should be set to be).
     * @see MessageStatus
     */
    MessageStatus evaluate(String answer);

    /**
     * Method called when anything goes wrong and the
     * <code>Message</code> couldn't be sent properly or an error has
     * occurred during the sending of the message.
     */
    void killing();
}
