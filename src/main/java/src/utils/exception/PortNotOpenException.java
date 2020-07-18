package src.utils.exception;

/**
 * Exception thrown when there exists the selected port but cannot be opened
 * or when the desired port is not opened.
 */
public class PortNotOpenException extends Exception {

    private final String[] objects;

    private final String message;

//    /**
//     * Constructor giving a String description of the cause of the problem.
//     *
//     * @param s (a String describing the problem).
//     */
//    public PortNotOpenException(String s){
//        super(s);
//        message = s;
//        objects = null;
//    }

    public PortNotOpenException(String message, String[] objects){
        this.message = message;
        this.objects = objects;
    }

    public String getMessage(){
        return message;
    }

    public String[] getPayload(){
        return objects;
    }
}
