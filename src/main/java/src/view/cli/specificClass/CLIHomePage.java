package src.view.cli.specificClass;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import src.app.MainApp;
import src.controller.Status;
import src.utils.connection.Connection;
import src.utils.exception.NotSuchPropertyException;
import src.utils.exception.PropertyLoadException;
import src.utils.properties.MyProperty;

import java.util.InputMismatchException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Scanner;

public class CLIHomePage extends CLISpecificClass {

    private static final Logger logger = LogManager.getLogger("view");

    private Connection myConnection;

    private ResourceBundle messages;

    public CLIHomePage(Locale currentLocale, MyProperty myProperty, Connection myConnection){
        this.setLocale(currentLocale);
        this.setMyProperty(myProperty);

        this.myConnection = myConnection;

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

        while(sel<0||sel>3){
            System.out.println(messages.getString(myProperty.safeGetProperty("title")));

            System.out.println("0- " + messages.getString(myProperty.safeGetProperty("settings")));
            System.out.println("1- " + messages.getString(myProperty.safeGetProperty("startRace")));
            System.out.println("2- " + messages.getString(myProperty.safeGetProperty("fastStart")));
            System.out.println("3- " + messages.getString(myProperty.safeGetProperty("close")));

            System.out.println(messages.getString(myProperty.safeGetProperty("choose")));

            try {
                sel = scanner.nextInt();
            }catch(InputMismatchException e){
                sel = -1;
            }

        }

        if(sel == 0){
            return Status.SETTINGS;
        }
        if(sel == 1){
            if(myConnection!=null)
                return Status.START_RACE;
        }
        if(sel == 2){
            if(myConnection!=null)
                return Status.START;
        }
        if(sel == 3){
            return Status.CLOSE;
        }

        return Status.HOME_PAGE;

    }
}
