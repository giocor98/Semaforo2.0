package src.view.cli;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import src.app.MainApp;
import src.utils.exception.NotSuchPropertyException;
import src.utils.exception.PropertyLoadException;
import src.utils.properties.MyProperty;
import src.view.View;
import src.view.cli.specificClass.CLISelectPort;

import java.text.MessageFormat;
import java.util.*;

// TODO: 15/07/20 comment it

/**
 * Class implementing <code>View</code> for the CLI.
 *
 * @see View
 */
public class CLI extends View {

    private static final Logger logger = LogManager.getLogger("view");

    @Override
    public String selectPort(List<String> portList, String defaultPort) {

        CLISelectPort cliSelectPort = new CLISelectPort();
        cliSelectPort.setLocale(currentLocale);
        cliSelectPort.setMyProperty(myProperty.retrieveProperties("CLISelectPort"));

        return cliSelectPort.selectPort(portList, defaultPort);
    }

    @Override
    public void error(String errorMessageName, String[] payload) {
        //Retrieving the file for printing messages
        ResourceBundle messages;
        MessageFormat formatter = new MessageFormat("");
        formatter.setLocale(currentLocale);
        try {
            messages = ResourceBundle.getBundle(myProperty.getProperty("bundle.error"), currentLocale, this.getClass().getClassLoader());
        } catch (PropertyLoadException | NotSuchPropertyException e) {
            logger.fatal("Cannot read the bundle.file Property");
            MainApp.end();
            return;
        }

        formatter.applyPattern(messages.getString(errorMessageName));

        System.err.println(formatter.format(payload));

    }

    /**
     * <p>
     * Method to retrieve a <code>String</code> representation
     * of tis'type.
     * </p>
     *
     * @return (a <code>String</code> representation of this'type).
     */
    @Override
    public String getViewType() {
        return "CLI";
    }
}
