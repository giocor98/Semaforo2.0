package src.app;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.LoggerConfig;
import src.utils.connection.Connection;
import src.utils.exception.NotSuchPropertiesException;
import src.utils.exception.PortNotFoundException;
import src.utils.exception.PortNotOpenException;
import src.utils.exception.PropertyLoadException;
import src.utils.properties.AppProperty;
import src.utils.threadPool.FixedSafeThreadPool;
import src.utils.threadPool.ThreadPool;
import src.view.View;
import src.view.cli.CLI;
import src.view.gui.GUI;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

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
     * <p>
     * The thread pool for this execution.
     * </p>
     */
    private ThreadPool threadPool;

    /**
     * <p>
     * The <code>Locale</code> relative to this execution used to have the right language
     * set for the user. It uses <code>i18n</code> as standard.
     * </p><p>
     * <a href="https://docs.oracle.com/javase/tutorial/i18n/index.html">the i18n tutorial</a>
     * </p>
     */
    private Locale currentLocale;

    /**
     * <p>
     * The <code>Logger</code> relative to the whole Class managing the main program-flow
     * logs. It uses the <code>log4j2</code> log.
     * </p>
     * <p><a href="https://logging.apache.org/log4j/2.x/">log4j2 ref</a></p>
     */
    private static final Logger logger = LogManager.getLogger("mainLogger");

    /**
     * <p>
     * Method to retrieve this' <code>ThreadPool</code>.
     * </p>
     *
     *
     * @return (this' <code>ThreadPool</code>).
     * @see    #threadPool
     */
    public ThreadPool getThreadPool() {
        return threadPool;
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
        //######INITIALISATION

        //##log notification
        logger.trace("App started");

        //##build AppProperty
        if(!AppProperty.build()){
            logger.fatal("Error building AppProperty");
            end();
            return;
        }

        //######PARSING ARGUMENTS

        List<String> arguments;

        //####Creating Argument List
        //##concatenating the list of passed arguments with the default list of arguments (retrieved from properties).
        try {
            arguments = Arrays.asList(AppProperty.getProperty("defaultParam.param").split(" "));
            Collections.addAll(arguments, args);
        } catch (NotSuchPropertiesException | PropertyLoadException e) {
            logger.fatal(e);
            logger.fatal("Error loading defaultParam.param.");
            end();
            return;
        }

        logger.trace(arguments);

        //####Reading Argument List
        //##creating arguments variales
        String viewType = null;
        String lang = null;
        String loggerLevel = null;

        //##Parsing the arguments
        for(String arg: arguments){
            switch (arg){
                case "--cli":
                    //#setting the viewer to be cli
                    viewType = "cli";
                    break;
                case "--gui":
                    //#setting the viewer to be gui
                    viewType = "gui";
                    break;
                case "--debug":
                    //#setting the log level to be trace
                    loggerLevel = "trace";

                    break;
                default:
                    String[] splitted = arg.replace(";", " ").split(" ");
                    switch (splitted[0]){
                        case "--lang":
                            //#setting the language
                            lang = splitted[1];
                            break;
                        case "--log":
                            //#setting the log level to be set
                            loggerLevel = splitted[1].toLowerCase();
                        default:
                            logger.warn("Wrong argument found: " + arg);
                    }
            }
        }

        //####Setting The Various Parameters Passed By Arguments

        //##setting log level
        if(loggerLevel!= null){
            Level level = null;

            //#retrieving the right Level
            switch(loggerLevel) {
                case "trace":
                    level = Level.TRACE;
                    break;
                case "debug":
                    level = Level.DEBUG;
                    break;
                case "warn":
                    level = Level.WARN;
                    break;
                case "error":
                    level = Level.ERROR;
                    break;
                case "fatal":
                    level = Level.FATAL;
                    break;
                default:
                    logger.error("Wrong loggerLevel found: " + loggerLevel);
            }

            //#setting the log level
            if(level!=null) {
                LoggerContext ctx = (LoggerContext) LogManager.getContext(false);

                logger.trace("Switching logging level from " + ctx.getRootLogger().getLevel());
                LoggerConfig config = ctx.getConfiguration().getRootLogger();
                config.setLevel(level);
                ctx.updateLoggers();  // This causes all Loggers to refetch information from their LoggerConfig.
                logger.trace("Switched logging level to " + ctx.getRootLogger().getLevel());
            }else{
                logger.error("Logger level not found: " + loggerLevel);
                logger.error("The passed arguments are: " + arguments);
            }
        }

        //##Setting ViewType
        View view;

        //#setting viewType if not already set with arguments
        if(viewType==null){
            logger.debug("viewer not loaded with parameters: going to retrieve it");
            try{
                viewType = AppProperty.getProperty("defaultParam.viewer");
            } catch (NotSuchPropertiesException | NullPointerException e) {
                logger.fatal("Property \"defaultParam.viewer\" not found");
                logger.fatal(e);
                end();
                return;
            } catch (PropertyLoadException e) {
                logger.fatal("Properties \"defaultParam\" not found");
                logger.fatal(e);
                end();
                return;
            }

            logger.debug("Viewer set to: " + viewType);
        }

        //#Setting view depending on the viewType previously set
        switch (viewType){
            case "gui":
                view = new GUI();
                break;
            case "cli":
                view = new CLI();
                break;
            default:
                logger.fatal("No valid viewer set: " + viewType);
                end();
                return;
        }

        //##setting currentLocale
        Locale currentLocale;

        //#setting lang if not already set with arguments
        if(lang == null){
            try {
                lang = AppProperty.getProperty("defaultParam.lang");
                if(lang == null)
                    throw new NullPointerException();
            } catch (NotSuchPropertiesException | PropertyLoadException | NullPointerException e) {
                logger.fatal(e);
                logger.fatal("Cannot load \"defaultParam.lang\".");
                end();
                return;
            }
        }

        //#setting currentLocale
        if(lang.equals("")){
            //setting it with the System default value
            currentLocale = Locale.getDefault();
            logger.debug("setting default currentLocale: " + currentLocale.getDisplayName());
        }else{
            //setting it with the passed lang.
            currentLocale = Locale.forLanguageTag(lang);
            logger.debug("setting currentLocale with lang: " + lang + " to: " + currentLocale.getDisplayName());
        }

        //######BUILDING THE MAIN APP
        builder(view, currentLocale);

        //######EXECUTING MAIN APP

        //foo instruction
        System.out.println("Hello World!");

        //######FINISHING
        end();

    }

    /**
     * Method to be called to close the program correctly.
     */
    public static void end(){
        Connection.closeAll();
        System.exit(0);
    }

    private static MainApp builder(View view, Locale currentLocale){
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

        return new MainApp(view, connection, currentLocale);
    }

    protected MainApp(View view, Connection connection, Locale currentLocale){
        this.view = view;
        this.connection = connection;
        this.currentLocale = currentLocale;
        this.threadPool = new FixedSafeThreadPool();

        AppProperty.build();
    }
}
