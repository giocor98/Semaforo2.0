package src.app;

import src.utils.connection.Connection;
import src.utils.exception.NotSuchPropertiesException;
import src.utils.exception.PortNotFoundException;
import src.utils.exception.PortNotOpenException;
import src.utils.exception.PropertyLoadException;
import src.utils.properties.AppProperty;
import src.utils.threadPool.FixedSafeThreadPool;
import src.utils.threadPool.ThreadPool;
import src.view.View;

import java.util.Arrays;
import java.util.Collections;
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

    /**
     * The thread pool for this execution.
     */
    private ThreadPool threadPool;

    public ThreadPool getThreadPool() {
        return threadPool;
    }

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
        //initialise the log

        //initialise the configReaders
        if(!AppProperty.build()){
            System.err.println("Errore nell'apertura delle properties");
            end();
        }

        //creating a list of arguments
        List<String> arguments = null;
        try {
            arguments = Arrays.asList(AppProperty.getProperty("defaultParam.param").split(" "));
            Collections.addAll(arguments, args);
        } catch (NotSuchPropertiesException | PropertyLoadException e) {
            System.err.println("Errore nell'apertura delle properties");
            e.printStackTrace();
            end();
            return;
        }

        System.out.println(arguments);

        //creating arguments variales
        ViewType viewType = null;

        //Parsing the arguments
        for(String arg: arguments){
            switch (arg){
                case "--cli":
                    viewType = ViewType.CLI;
                    break;
                case "--gui":
                    viewType = ViewType.GUI;
                    break;
            }
        }

        if(viewType==null){
            System.err.println("No viewer selected");
            return;
        }

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
        this.threadPool = new FixedSafeThreadPool();

        AppProperty.build();
    }
}
