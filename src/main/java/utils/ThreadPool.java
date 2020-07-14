package utils;

import app.MainApp;

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
     * <p>
     * Method to safeSubmit a task to the tread pool.
     * </p><p>
     * If the tasks throw an unchecked exception, it 'll terminate the whole program
     * after printing the stackTrace.
     * </p>
     *
     * @param runnable (the task to be executed).
     */
    public static void safeSubmit(Runnable runnable){
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
