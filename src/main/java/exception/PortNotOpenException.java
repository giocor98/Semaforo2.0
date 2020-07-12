package exception;

/**
 * Exception thrown when there exists the selected port but cannot be opened
 * or when the desired port is not opened.
 */
public class PortNotOpenException extends Exception {

    /**
     * Constructor giving a String description of the cause of the problem.
     *
     * @param s (a String describing the problem).
     */
    public PortNotOpenException(String s){
        super(s);
    }
}
