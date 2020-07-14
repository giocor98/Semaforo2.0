package src.utils.threadPool;

import src.app.MainApp;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Class to manage the pool of threads.
 */
public class FixedSafeThreadPool implements  ThreadPool{

    /**
     * <p>
     * Constant indicating the number of <code>Threads</code> with which populate
     * pool. Now set to 5.
     * </p>
     */
    private static final int nThreads = 5;

    /**
     * the pool of threads
     */
    ExecutorService pool;

    /**
     * <p>
     * Method to initialise the <code>FixedSafeThreadPool</code>.
     * </p>
     */
    public FixedSafeThreadPool(int numberOfThreads){
        pool =Executors.newFixedThreadPool(numberOfThreads);
    }

    public FixedSafeThreadPool(){
        this(nThreads);
    }

    /**
     * <p>
     * Method to safeSubmit a task to the tread pool.
     * </p><p>
     * If the tasks throw an unchecked src.utils.exception, it 'll terminate the whole program
     * after printing the stackTrace.
     * </p>
     *
     * @param runnable (the task to be executed).
     */
    public void submit(Runnable runnable){
        //Submit the task ti the pool.
        pool.submit(() -> {
            try {
                runnable.run();
            }catch(Exception e){
                // TODO: 14/07/20 gestire l'errore 
                System.err.println("Uncatched Exception:");
                e.printStackTrace();
                MainApp.end();
            }
        });
    }
}
