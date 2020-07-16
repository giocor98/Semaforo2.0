package src.view.gui.specificClass;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import src.app.MainApp;
import src.utils.exception.NotSuchPropertyException;
import src.utils.exception.PropertyLoadException;
import src.view.gui.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.MessageFormat;
import java.util.List;
import java.util.ResourceBundle;

public class GUISelectPort extends GUISpecificClass {

    private static final Logger logger = LogManager.getLogger("view");

    private Boolean useDefault = false;

    private String answer = "";

    private final Object lock = new Object();

    public String selectPort(List<String> portList, String defaultPort){

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

            JFrame frame = GUI.buidFrame(messages.getString(myProperty.safeGetProperty("title")), WindowConstants.EXIT_ON_CLOSE);

            JPanel content = new JPanel();
            content.setLayout(new BorderLayout());

            formatter.applyPattern(messages.getString(myProperty.safeGetProperty("selectPort")));
            content.add(new JPanel().add(new JLabel(formatter.format(new String[]{defaultPort}))), BorderLayout.NORTH);

            JPanel southPanel = new JPanel();
            content.add(southPanel, BorderLayout.SOUTH);

            JButton yesButton = new JButton(messages.getString(myProperty.safeGetProperty("yes")));
            yesButton.addActionListener(e -> {
                useDefault = true;
                synchronized (lock) {
                    lock.notifyAll();
                }
            });
            southPanel.add(yesButton);

            JButton noButton = new JButton(messages.getString(myProperty.safeGetProperty("no")));
            noButton.addActionListener(e -> {
                useDefault = false;
                synchronized (lock) {
                    lock.notifyAll();
                }
            });
            southPanel.add(noButton);

            frame.add(content);

            frame.pack();
            frame.setVisible(true);

            synchronized (lock){
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    logger.error(e);
                }
            }

            frame.dispose();
            if(useDefault)
                return defaultPort;

        }

        JFrame frame = GUI.buidFrame(messages.getString(myProperty.safeGetProperty("title")), WindowConstants.EXIT_ON_CLOSE);

        JPanel content = new JPanel();
        content.setLayout(new BorderLayout());

        content.add(new JPanel().add(new JLabel(messages.getString(myProperty.safeGetProperty("selectFromList")))), BorderLayout.NORTH);

        JPanel southPanel = new JPanel();
        content.add(southPanel, BorderLayout.SOUTH);

        JComboBox<String> porteBox = new JComboBox<>();
        porteBox.addItem(messages.getString(myProperty.safeGetProperty("noPort")));
        porteBox.addItem(messages.getString(myProperty.safeGetProperty("refresh")));
        for(String item: portList)
            porteBox.addItem(item);

        southPanel.add(porteBox);

        JButton selectButton = new JButton(myProperty.safeGetProperty("select"));
        selectButton.addActionListener(e -> {
            answer = porteBox.getSelectedItem().toString();
            synchronized (lock) {
                lock.notifyAll();
            }
        });

        southPanel.add(selectButton);

        frame.add(content);

        frame.pack();
        frame.setVisible(true);

        synchronized (lock){
            try {
                lock.wait();
            } catch (InterruptedException e) {
                logger.error(e);
            }
        }

        frame.dispose();

        if(answer.equals(messages.getString(myProperty.safeGetProperty("noPort")))){
            logger.trace("No port chosen");
            return null;
        } else if(answer.equals(messages.getString(myProperty.safeGetProperty("refresh")))){
            logger.trace("Refresh port list");
            return "";
        }
        logger.trace("selected: " + answer);
        return answer;
    }
}
