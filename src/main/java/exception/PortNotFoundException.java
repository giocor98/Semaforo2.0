package exception;

/**
 * Exception thrown when there isn't available the searched port.
 */
public class PortNotFoundException extends Exception {

    /**
     * Constructor giving a String description of the cause of the problem.
     *
     * @param s (a String describing the problem).
     */
    public PortNotFoundException(String s){
        super(s);
    }
}
