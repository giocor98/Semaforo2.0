package src.view.gui;

import com.fazecast.jSerialComm.SerialPort;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import src.app.MainApp;
import src.utils.exception.NotSuchPropertyException;
import src.utils.exception.PropertyLoadException;
import src.utils.properties.MyProperty;
import src.view.View;
import src.view.gui.specificClass.GUISelectPort;
import src.view.gui.specificClass.GUIWaiting;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.MessageFormat;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

// TODO: 15/07/20 comment it 

public class GUI extends View {

    private static final Logger logger = LogManager.getLogger("view");

    private JFrame frame;

    @Override
    public String selectPort(List<String> portList, String defaultPort) {
        GUISelectPort guiSelectPort = new GUISelectPort();
        guiSelectPort.setLocale(currentLocale);
        guiSelectPort.setMyProperty(myProperty.retrieveProperties("GUISelectPort"));

        return guiSelectPort.selectPort(portList, defaultPort);
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
        JOptionPane.showMessageDialog(null, formatter.format(payload), errorMessageName, JOptionPane.ERROR_MESSAGE);

    }

    @Override
    public void setWaiting(String status, int percentage) {
        setWaiting(status);
        setWaiting(percentage);
    }

    @Override
    public void setWaiting(String status) {
        GUIWaiting.getInstance().setMessage(status);
    }

    @Override
    public void setWaiting(int percentage) {
        GUIWaiting.getInstance().setPercentage(percentage);
    }

    @Override
    public void waiting() {
        this.frame = GUIWaiting.build(currentLocale, myProperty.retrieveProperties("GUIWaiting"));
        GUIWaiting.getInstance().waiting();
    }

    @Override
    public void init() {

    }

    @Override
    public String getViewType() {
        return "GUI";
    }

    @Override
    public void waitingClear() {
        GUIWaiting.clear();
    }

    public static JFrame buidFrame(String title, int exitOnClose){
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(exitOnClose);
        if(exitOnClose == WindowConstants.EXIT_ON_CLOSE){
            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    super.windowClosing(e);
                    MainApp.end();
                }
            });
        }
        return frame;
    }
}
