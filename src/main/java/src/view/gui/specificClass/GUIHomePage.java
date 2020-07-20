package src.view.gui.specificClass;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import src.app.MainApp;
import src.controller.Status;
import src.utils.connection.Connection;
import src.utils.exception.NotSuchPropertyException;
import src.utils.exception.PropertyLoadException;
import src.utils.properties.MyProperty;
import src.view.gui.GUI;
import src.view.gui.utils.PanelComponent;
import src.view.gui.utils.SubPanel;

import javax.swing.*;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class GUIHomePage extends GUISpecificClass {

    private static final Logger logger = LogManager.getLogger("view");

    private Connection myConnection;

    private ResourceBundle messages;

    private final Object lock = new Object();

    public GUIHomePage(Locale currentLocale, MyProperty myProperty, Connection myConnection){
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

        JFrame frame = GUI.buidFrame(messages.getString(myProperty.safeGetProperty("title")), WindowConstants.EXIT_ON_CLOSE);

        setFrame(frame);
    }

    public Status show(){

        AtomicBoolean done = new AtomicBoolean(false);
        AtomicReference<Status> ret = new AtomicReference<Status>(Status.NONE);

        JPanel background = new SubPanel(1, 1, 0, 0);
        background.add(new PanelComponent(.6, .2, .2, 0, new JLabel(messages.getString(myProperty.safeGetProperty("title")))));

        JButton settingsButton = new JButton(messages.getString(myProperty.safeGetProperty("settings")));
        settingsButton.addActionListener(e -> {
            synchronized (lock){
                if(!done.get()){
                    done.set(true);
                    ret.set(Status.SETTINGS);
                }
                lock.notifyAll();
            }
        });
        background.add(new PanelComponent(.6, .2, .2, .2, settingsButton));

        JButton startRaceButton = new JButton(messages.getString(myProperty.safeGetProperty("startRace")));
        startRaceButton.addActionListener(e -> {
            synchronized (lock){
                if(!done.get()){
                    done.set(true);
                    if(myConnection!=null)
                        ret.set(Status.START_RACE);
                    else
                        ret.set(Status.HOME_PAGE);
                }
                lock.notifyAll();
            }
        });
        background.add(new PanelComponent(.6, .2, .2, .4, startRaceButton));

        JButton startButton = new JButton(messages.getString(myProperty.safeGetProperty("fastStart")));
        startButton.addActionListener(e -> {
            synchronized (lock){
                if(!done.get()){
                    done.set(true);
                    if(myConnection!=null)
                        ret.set(Status.START);
                    else
                        ret.set(Status.HOME_PAGE);
                }
                lock.notifyAll();
            }
        });
        background.add(new PanelComponent(.6, .2, .2, .6, startButton));

        JButton closeButton = new JButton(messages.getString(myProperty.safeGetProperty("close")));
        closeButton.addActionListener(e -> {
            synchronized (lock){
                if(!done.get()){
                    done.set(true);
                    ret.set(Status.CLOSE);
                }
                lock.notifyAll();
            }
        });
        background.add(new PanelComponent(.6, .2, .2, .8, closeButton));

        frame.add(background);

        frame.setSize(500, 500);
        frame.setVisible(true);

        synchronized (lock){
            try {
                lock.wait();
            } catch (InterruptedException ignore) {
            }
        }
        frame.dispose();
        return ret.get();
    }
}
