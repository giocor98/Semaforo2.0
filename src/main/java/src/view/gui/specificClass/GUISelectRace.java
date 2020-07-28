package src.view.gui.specificClass;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import src.app.MainApp;
import src.controller.Status;
import src.utils.connection.Connection;
import src.utils.exception.NotSuchPropertyException;
import src.utils.exception.PropertyLoadException;
import src.utils.properties.MyProperty;
import src.view.gui.utils.PanelComponent;
import src.view.gui.utils.SubPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Locale;
import java.util.ResourceBundle;

public class GUISelectRace extends GUISpecificClass {

    private static final Logger logger = LogManager.getLogger("view");

    private Status ret = null;

    private final Object lock = new Object();

    private ResourceBundle messages;

    public GUISelectRace(Locale currentLocale, MyProperty myProperty) {
        this.setLocale(currentLocale);
        this.setMyProperty(myProperty);

        try {
            messages = ResourceBundle.getBundle(myProperty.getProperty("bundle.file"), currentLocale, this.getClass().getClassLoader());
        } catch (PropertyLoadException | NotSuchPropertyException e) {
            logger.fatal("Cannot read the bundle.file Property");
            MainApp.end();
        }

    }

    public Status show(){
        JFrame frame = new JFrame(messages.getString(myProperty.safeGetProperty("title")));

        JPanel background = new SubPanel(1, 1, 0, 0);
        JLabel req = new JLabel(messages.getString(myProperty.safeGetProperty("request")));
        background.add(new PanelComponent(1, 0.5, 0, 0, req));

        JButton race = new JButton();
        race.setText(messages.getString(myProperty.safeGetProperty("race")));
        race.addActionListener(e -> {
            synchronized (lock){
                ret = Status.RACE_SEM;
                lock.notifyAll();
            }
            frame.dispose();
        }
        );
        background.add(new PanelComponent(.5, .5, 0, .5, race));

        JButton endurance = new JButton();
        endurance.setText(messages.getString(myProperty.safeGetProperty("endurance")));
        endurance.addActionListener(e -> {
                    synchronized (lock){
                        ret = Status.END_SEM;
                        lock.notifyAll();
                    }
                    frame.dispose();
                }
        );
        background.add(new PanelComponent(.5, .5, 0.5, .5, endurance));

        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {

            }

            @Override
            public void windowClosing(WindowEvent e) {
                synchronized (lock){
                    ret = Status.HOME_PAGE;
                    lock.notifyAll();
                }
            }

            @Override
            public void windowClosed(WindowEvent e) {

            }

            @Override
            public void windowIconified(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {

            }

            @Override
            public void windowDeactivated(WindowEvent e) {

            }
        });

        frame.add(background);
        frame.setSize(500, 300);
        frame.setVisible(true);

        synchronized (lock) {
            try {
                lock.wait();
            } catch (InterruptedException e) {
                logger.warn(e);
            }
        }
        return ret;
    }
}
