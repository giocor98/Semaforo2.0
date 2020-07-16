package src.view.gui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import src.app.MainApp;
import src.utils.properties.MyProperty;
import src.view.View;
import src.view.gui.specificClass.GUISelectPort;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.Locale;

// TODO: 15/07/20 comment it 

public class GUI extends View {

    private static final Logger logger = LogManager.getLogger("view");

    @Override
    public String selectPort(List<String> portList, String defaultPort) {
        GUISelectPort guiSelectPort = new GUISelectPort();
        guiSelectPort.setLocale(currentLocale);
        guiSelectPort.setMyProperty(myProperty.retrieveProperties("GUISelectPort"));

        return guiSelectPort.selectPort(portList, defaultPort);
    }

    @Override
    public void error(String errorMessage) {}

    @Override
    public String getViewType() {
        return "GUI";
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
