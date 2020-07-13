package connection.interfaces;

import connection.messages.MessageStatus;

/**
 * The default <code>AnswerEvaluator</code>.
 *
 * @see AnswerEvaluator
 */
public class DefaultEvaluator implements AnswerEvaluator {

    /**
     * <p>Method implementing the {@link  AnswerEvaluator#evaluate(String) AnswerEvaluator one}.</p>
     * <p>Method called on the arrival of an answer for the evaluation of it.</p>
     *
     * @param answer (the <code>String</code> answer received).
     * @return       (the next <code>MessageStatus</code> to which the
     *               <code>Message</code> should be set to be).
     * @see AnswerEvaluator
     * @see AnswerEvaluator#evaluate(String)
     * @see MessageStatus
     */
    @Override
    public MessageStatus evaluate(String answer) {
        if(answer.contains("A")) return MessageStatus.ACKED;
        if (answer.contains("E")) return MessageStatus.ERROR;
        return MessageStatus.RECEIVED;
    }

    /**
     * <p>
     * Method implementing the {@link  AnswerEvaluator#killing() AnswerEvaluator one}.
     * </p>
     * <p>
     * Method called when anything goes wrong and the
     * <code>Message</code> couldn't be sent properly or an error has
     * occurred during the sending of the message.
     * </p>
     *
     * @see AnswerEvaluator
     * @see AnswerEvaluator#killing()
     */
    @Override
    public void killing() {
        // TODO: 13/07/20 mettere un modo per gestire i log
        System.out.println("Message aborted");
    }
}
