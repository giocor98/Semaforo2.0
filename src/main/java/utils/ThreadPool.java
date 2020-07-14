package utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Class to manage the pool of threads.
 */
public class ThreadPool {

    /**
     * the pool of threads
     */
    static ExecutorService pool =Executors.newFixedThreadPool(5);

    /**
     * Method to submit a task to the tread pool.
     *
     * @param runnable (the task to be executed).
     */
    public static void submit(Runnable runnable){
        try {
            pool.submit(runnable);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
