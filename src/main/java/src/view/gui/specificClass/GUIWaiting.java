package src.view.gui.specificClass;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import src.app.MainApp;
import src.utils.exception.NotSuchPropertyException;
import src.utils.exception.PropertyLoadException;
import src.utils.properties.MyProperty;
import src.view.gui.GUI;
import src.view.gui.utils.ImagePanel;
import src.view.gui.utils.PanelComponent;
import src.view.gui.utils.SubPanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class GUIWaiting extends GUISpecificClass {

    private static final Logger logger = LogManager.getLogger("view");

    private static GUIWaiting instance;

    private ResourceBundle messages;
    private MessageFormat formatter;

    private JLabel message;
    private JLabel percentage;

    private GUIWaiting(Locale locale, MyProperty myProperty){
        setLocale(locale);
        setMyProperty(myProperty);

        formatter = new MessageFormat("");
        formatter.setLocale(currentLocale);
        try {
            messages = ResourceBundle.getBundle(myProperty.getProperty("bundle.file"), currentLocale, this.getClass().getClassLoader());
        } catch (PropertyLoadException | NotSuchPropertyException e) {
            logger.fatal("Cannot read the bundle.file Property");
            MainApp.end();
        }

        JFrame frame = GUI.buidFrame(messages.getString(myProperty.safeGetProperty("title")), WindowConstants.EXIT_ON_CLOSE);

        setFrame(frame);


    }

    public static JFrame build(Locale locale, MyProperty myProperty){
        instance = new GUIWaiting(locale, myProperty);
        return instance.frame;
    }

    public static GUIWaiting getInstance(){
        return instance;
    }

    public void waiting(){
        //frame.setLayout(new BorderLayout());

        JPanel backgrond = new SubPanel(1, 1, 0, 0);


        formatter.applyPattern(messages.getString(myProperty.safeGetProperty("formatted")));
        percentage = new JLabel(formatter.format(new String[]{"0"}));

        backgrond.add(new PanelComponent(1, .1, 0, 0, percentage));

        //frame.add(percentage, BorderLayout.NORTH);

        backgrond.add(new PanelComponent(.5, .8, 0, .1, new ImagePanel(myProperty.safeGetProperty("eastSponsor"))));
        backgrond.add(new PanelComponent(.5, .8, .5, .1, new ImagePanel(myProperty.safeGetProperty("westSponsor"))));
        //frame.add(new ImagePanel(myProperty.safeGetProperty("eastSponsor")), BorderLayout.EAST);
        //frame.add(new ImagePanel(myProperty.safeGetProperty("westSponsor")), BorderLayout.WEST);

        message = new JLabel(messages.getString(myProperty.safeGetProperty("initialisation")));

        backgrond.add(new PanelComponent(1, .1, 0, .9, message));
        //frame.add(message, BorderLayout.SOUTH);

        frame.add(backgrond);

        frame.setSize(500, 500);
        frame.setVisible(true);
    }

    public void setPercentage(Integer percentageSet){
        formatter.applyPattern(messages.getString(myProperty.safeGetProperty("formatted")));
        percentage.setText(formatter.format(new String[]{percentageSet.toString()}));
    }

    public void setMessage(String messageName){
        message.setText(messages.getString(myProperty.safeGetProperty(messageName)));
    }

    public static void clear(){
        instance = null;
    }

}
