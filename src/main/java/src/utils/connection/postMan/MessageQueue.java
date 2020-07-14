package src.utils.connection.postMan;

import src.utils.connection.messages.Message;

import java.util.NoSuchElementException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * <p>
 * Queue of <code>Message</code> used to have an ordered queue
 * with all the <code>Message</code> remaining to be sent.
 * </p>
 * <p>
 * It has inside a Blocking queue and notify a thread to send the
 * <code>Message</code>.
 * </p>
 */
class MessageQueue{

    /**
     * The <code>BlockingQueue</code> that contains all the <code>Message</code>
     * that needs to be sent.
     */
    private BlockingQueue<Message> queue = new ArrayBlockingQueue<>(32);

    /**
     * Method to enqueue the given <code>Message</code> to this.
     *
     * @param message (the <code>Message</code> to be enqueued).
     * @throws IllegalStateException (if the <code>BlockingQueue</code> is full).
     */
    public void add(Message message) throws IllegalStateException {
        this.queue.add(message);
    }


    /**
     * Method that checks if there is at least one <code>Message</code>
     * to be sent.
     *
     * @return (true iif there is a <code>Message</code> to be sent).
     */
    public boolean available(){
        try{
            this.queue.element();
            return true;
        }catch (NoSuchElementException e){
            return false;
        }
    }

    /**
     * Method to retrieve the next <code>Message</code> to be set.
     *
     * @return (the next <code>Message</code> to be set).
     * @throws NoSuchElementException (if there is no <code>Message</code>
     *                                to be send).
     */
    public Message get() throws NoSuchElementException {
        return this.queue.remove();
    }

}