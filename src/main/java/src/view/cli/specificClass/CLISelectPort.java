package src.view.cli.specificClass;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import src.app.MainApp;
import src.utils.exception.NotSuchPropertyException;
import src.utils.exception.PropertyLoadException;

import java.text.MessageFormat;
import java.util.InputMismatchException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;

public class CLISelectPort extends CLISpecificClass {

    private static final Logger logger = LogManager.getLogger("view");

    public String selectPort(List<String> portList, String defaultPort){
        //create the scanner
        Scanner scanner = new Scanner(System.in);

        //Retrieving the file for printing messages
        ResourceBundle messages;
        MessageFormat formatter = new MessageFormat("");
        formatter.setLocale(currentLocale);
        try {
            messages = ResourceBundle.getBundle(myProperty.getProperty("bundle.file"), currentLocale, this.getClass().getClassLoader());
        } catch (PropertyLoadException | NotSuchPropertyException e) {
            logger.fatal("Cannot read the bundle.file Property");
            MainApp.end();
            return null;
        }

        //Requesting for the default port
        if (defaultPort != null) {

            String answ = "";
            while(!answ.contains(messages.getString(myProperty.safeGetProperty("yesChar"))) && !answ.contains((messages.getString(myProperty.safeGetProperty("noChar"))))){
                formatter.applyPattern(messages.getString(myProperty.safeGetProperty("selectPort")));
                System.out.println(formatter.format(new String[]{defaultPort}));

                System.out.println(messages.getString(myProperty.safeGetProperty("yesChar")) + ": " + messages.getString(myProperty.safeGetProperty("yes"))
                        + "\t" + messages.getString(myProperty.safeGetProperty("noChar")) + ": " + messages.getString(myProperty.safeGetProperty("no")));

                answ = scanner.next();
            }
            if(answ.contains(messages.getString(myProperty.safeGetProperty("yesChar")))){
                return defaultPort;
            }

        }

        int ret = -1;

        //printing the options
        System.out.println(messages.getString(myProperty.safeGetProperty("selectFromList")));
        System.out.println("-1\t" + messages.getString(myProperty.safeGetProperty("noPort")));
        System.out.println("0\t" + messages.getString(myProperty.safeGetProperty("refresh")));
        for(int i=0; i<portList.size(); i++){
            System.out.println((i+1) + "\t" + portList.get(i));
        }

        //reading the answer
        System.out.println(messages.getString(myProperty.safeGetProperty("selectRequest")));
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

}
