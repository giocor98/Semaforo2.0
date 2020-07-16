package src.view.cli;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import src.app.MainApp;
import src.utils.exception.NotSuchPropertyException;
import src.utils.exception.PropertyLoadException;
import src.utils.properties.MyProperty;
import src.view.View;

import java.text.MessageFormat;
import java.util.*;

// TODO: 15/07/20 comment it

/**
 * Class implementing <code>View</code> for the CLI.
 *
 * @see View
 */
public class CLI implements View {

    private static final Logger logger = LogManager.getLogger("view");

    private Locale actualLocale;

    private MyProperty cliProperty;

    @Override
    public String selectPort(List<String> portList, String defaultPort) {

        //create the scanner
        Scanner scanner = new Scanner(System.in);

        //Retrieving the file for printing messages
        ResourceBundle messages;
        MessageFormat formatter = new MessageFormat("");
        formatter.setLocale(actualLocale);
        try {
            messages = ResourceBundle.getBundle(cliProperty.getProperty("selectPort"), actualLocale, this.getClass().getClassLoader());
        } catch (PropertyLoadException | NotSuchPropertyException e) {
            logger.fatal("Cannot read the Property");
            MainApp.end();
            return null;
        }

        //Requesting for the default port
        if (defaultPort != null) {

            String answ = "";
            while(!answ.contains(messages.getString("YesChar")) && !answ.contains((messages.getString("noChar")))){
                formatter.applyPattern(messages.getString("selectPort"));
                System.out.println(formatter.format(new String[]{defaultPort}));

                System.out.println(messages.getString("yesChar") + ": " + messages.getString("yes")
                        + "\t" + messages.getString("noChar") + ": " + messages.getString("no"));

                answ = scanner.next();
            }
            if(answ.contains(messages.getString("yesChar"))){
                return defaultPort;
            }

        }

        int ret = -1;

        //printing the options
        System.out.println(messages.getString("selectFromList"));
        System.out.println("-1\t" + messages.getString("noPort"));
        System.out.println("0\t" + messages.getString("refresh"));
        for(int i=0; i<portList.size(); i++){
            System.out.println((i+1) + "\t" + portList.get(i));
        }

        //reading the answer
        System.out.println(messages.getString("selectRequest"));
        try {
            ret = scanner.nextInt() - 1;
        }catch(InputMismatchException e){
            ret = -1;
        }

        //checking if answer was "-1" => return null
        if(ret == -2)
            return null;

        //Checking if the answer was 0 or a not valid one => returns ""
        if(ret<0||ret>=portList.size())
            return "";

        //returns the selected port.
        return portList.get(ret);
    }

    @Override
    public void error(String errorMessage) {
        System.err.println(errorMessage);
    }

    /**
     * <p>
     * Method to set the current <code>Locale</code>
     * </p>
     *
     * @param locale (the <code>Locale</code> to be set as current).
     */
    @Override
    public void setLocale(Locale locale) {
        this.actualLocale = locale;
    }

    /**
     * <p>
     * Method to set this' <code>MyProperty</code>.
     * </p>
     * @param myProperty (this' <code>MyProperty</code>).
     */
    @Override
    public void setMyProperty(MyProperty myProperty) {
        this.cliProperty = myProperty;
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
