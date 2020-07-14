package src.utils.threadPool;

import src.app.MainApp;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Class to manage the pool of threads.
 */
public class ThreadPool {

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
    static ExecutorService pool;

    /**
     * <p>
     * Method to initialise the <code>ThreadPool</code>.
     * </p>
     */
    public static void init(){
        if(pool!=null)
            pool =Executors.newFixedThreadPool(nThreads);
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
    public static void safeSubmit(Runnable runnable){
        //check if pool has been initialised, else initialise it.
        if(pool==null)
            init();
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
