package src.app;

import src.utils.connection.Connection;
import src.utils.exception.PortNotFoundException;
import src.utils.exception.PortNotOpenException;
import src.utils.threadPool.ThreadPool;
import src.view.View;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class to manage the program life
 */
public class MainApp {

    /**
     * <p>
     * The <code>Connection</code> relative to this program
     * execution.
     * </p>
     */
    private Connection connection;

    /**
     * <p>
     * The <code>View</code> relative to this program
     * execution.
     * </p>
     */
    private View view;

    private enum ViewType{
        CLI {
            View getView(){
                return new src.view.cli.CLI();
            }
        },
        GUI{
            View getView(){
                return new src.view.gui.GUI();
            }
        };
        abstract View getView();
    }

    /**
     * <p>
     * Main method to launch the program.
     * </p><p>
     * It creates and initialises the <code>MainApp</code>
     * instance that will lead the program flow.
     * </p>
     * <p>AVAILABLE ARGUMENTS:</p>
     * <p>
     * <code>--cli</code> to run the cli as the viewer.
     * </p>
     * <p>
     * <code>--gui</code> to run the gui as the viewer.
     * </p>
     *
     * @param args (list of arguments passed to the program).
     */
    public static void main(String[] args) {
        //arguments parameters
        ViewType viewType = ViewType.CLI;

        //creating a list of arguments
        List<String> arguments = Arrays.stream(args).collect(Collectors.toList());

        //Parsing the arguments
        if(arguments.contains("--gui"))
            viewType = ViewType.CLI;
        if(arguments.contains("--cli"))
            viewType = ViewType.GUI;

        //Creating the MainApp instance.
        builder(viewType.getView());

        //foo instruction
        System.out.println("Hello World!");

        end();

    }

    /**
     * Method to be called to close the program correctly.
     */
    public static void end(){
        Connection.closeAll();
        System.exit(0);
    }

    private static MainApp builder(View view){
        Connection connection;

        String port;

        while(true){
            port = view.selectPort(Connection.availablePorts());
            if(port == null){
                connection = null;
                break;
            }
            if(port.equals(""))
                continue;
            try{
                connection = new Connection(port);
                break;
            }catch (PortNotFoundException | PortNotOpenException e){
                view.error(e.getMessage());
            }
        }

        return new MainApp(view, connection);
    }

    protected MainApp(View view, Connection connection){
        this.view = view;
        this.connection = connection;
        ThreadPool.init();
    }
}
