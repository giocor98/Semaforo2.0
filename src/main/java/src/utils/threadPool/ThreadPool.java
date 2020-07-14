package src.utils.threadPool;

/**
 * Interface to manage the pool of threads.
 */
public interface ThreadPool {
    /**
     * <p>
     * Method to notify a <code>Thread</code> from the pool to execute
     * the task passed as argument.
     * </p>
     *
     * @param runnable (the <code>Runnable</code> task to be executed).
     */
    public void submit(Runnable runnable);
}
