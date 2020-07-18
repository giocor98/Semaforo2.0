package src.utils.exception;

/**
 * Exception thrown when there isn't available the searched port.
 */
public class PortNotFoundException extends Exception {

    private final String[] objects;

    private final String message;

    public PortNotFoundException(String message, String[] objects){
        super(message);
        this.message = message;
        this.objects = objects;
    }

//    /**
//     * Constructor giving a String description of the cause of the problem.
//     *
//     * @param s (a String describing the problem).
//     */
//    public PortNotFoundException(String s){
//        super(s);
//        message = s;
//        objects = null;
//    }

    public String getMessage(){
        return message;
    }

    public String[] getPayload(){
        return objects;
    }
}
