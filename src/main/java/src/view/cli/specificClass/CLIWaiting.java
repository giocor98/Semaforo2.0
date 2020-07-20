package src.view.cli.specificClass;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import src.app.MainApp;
import src.utils.exception.NotSuchPropertyException;
import src.utils.exception.PropertyLoadException;
import src.utils.properties.MyProperty;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class CLIWaiting extends CLISpecificClass {

    private static final Logger logger = LogManager.getLogger("view");

    private static CLIWaiting instance;

    private String message;
    private Integer percentage;

    private ResourceBundle messages;
    private MessageFormat formatter;

    public static void waiting(Locale currentLocale, MyProperty myProperty){
        instance = new CLIWaiting(currentLocale, myProperty);
    }

    private CLIWaiting(Locale currentLocale, MyProperty myProperty){
        this.setLocale(currentLocale);
        this.setMyProperty(myProperty);

        //Retrieving the file for printing messages

        formatter = new MessageFormat("");
        formatter.setLocale(currentLocale);
        try {
            messages = ResourceBundle.getBundle(myProperty.getProperty("bundle.file"), currentLocale, this.getClass().getClassLoader());
        } catch (PropertyLoadException | NotSuchPropertyException e) {
            logger.fatal("Cannot read the bundle.file Property");
            MainApp.end();
        }
    }

    public static CLIWaiting getInstance(){
        return instance;
    }

    public void setWaiting(int percentage){
        this.percentage = percentage;
        show();
    }

    public void setWaiting(String message){
        this.message = message;
        show();
    }

    public void setWaiting(String message, int percentage){
        this.percentage = percentage;
        this.message = message;
        show();
    }

    private void show(){
        String tmp;
        formatter.applyPattern(messages.getString(myProperty.safeGetProperty("formatted")));
        try{
            tmp = messages.getString(myProperty.safeGetProperty(this.message));
        }catch(MissingResourceException e){
            logger.warn(e);
            logger.warn("Cannot load string " + this.message);
            tmp = this.message;
        }
        System.out.println(formatter.format(new String[]{tmp, this.percentage.toString()}));
    }

}
