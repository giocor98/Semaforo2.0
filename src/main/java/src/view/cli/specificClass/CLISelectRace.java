package src.view.cli.specificClass;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import src.app.MainApp;
import src.controller.Status;
import src.utils.exception.NotSuchPropertyException;
import src.utils.exception.PropertyLoadException;
import src.utils.properties.MyProperty;

import java.util.InputMismatchException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Scanner;

public class CLISelectRace extends CLISpecificClass {

    private ResourceBundle messages;

    private static final Logger logger = LogManager.getLogger("view");

    public CLISelectRace(Locale currentLocale, MyProperty myProperty){
        this.setLocale(currentLocale);
        this.setMyProperty(myProperty);

        //Retrieving the file for printing messages
        try {
            messages = ResourceBundle.getBundle(myProperty.getProperty("bundle.file"), currentLocale, this.getClass().getClassLoader());
        } catch (PropertyLoadException | NotSuchPropertyException e) {
            logger.fatal("Cannot read the bundle.file Property");
            MainApp.end();
        }
    }

    public Status show(){
        int sel = -1;
        Scanner scanner = new Scanner(System.in);

        while(sel<0||sel>2){
            System.out.println(messages.getString(myProperty.safeGetProperty("request")));

            System.out.println("0- " + messages.getString(myProperty.safeGetProperty("home")));
            System.out.println("1- " + messages.getString(myProperty.safeGetProperty("race")));
            System.out.println("2- " + messages.getString(myProperty.safeGetProperty("endurance")));

            try {
                sel = scanner.nextInt();
            }catch(InputMismatchException e){
                sel = -1;
            }

        }
        if(sel == 0){
            return Status.HOME_PAGE;
        }
        if(sel == 1){
            return Status.RACE_SEM;
        }
        return Status.END_SEM;
    }
}
