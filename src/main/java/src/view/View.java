package src.view;

import src.controller.Status;
import src.utils.connection.Connection;
import src.utils.properties.MyProperty;

import java.util.List;
import java.util.Locale;

/**
 * Interface working as a facade for the src.view package.
 */
public abstract class View {

    protected MyProperty myProperty;
    protected Locale currentLocale;

    /**
     * <p>
     * Method called to select one port from the list passed as argument.
     * </p><p>
     * It must return the port selected by the user, or "" to refresh the
     * selection, or <code>null</code> if the user wants not to use a port.
     * </p><p>
     * The defaultPort is the default ort to be opened. It'll be null if
     * it is not available.
     * </p>
     *
     * @param portList (the list of port available to the choice).
     * @return (the user selected port).
     */
    public abstract String selectPort(List<String> portList, String defaultPort);

    /**
     * <p>
     * Method called to display an error message to the user.
     * </p>
     *
     * @param errorMessageName (the error message to be displayed).
     */
    public abstract void error(String errorMessageName, String[] payload);

    public abstract void setWaiting(String status, int percentage);

    public abstract void setWaiting(String status);

    public abstract void setWaiting(int percentage);

    public abstract void waiting();

    public abstract Status homePage(Connection connection);

    public abstract void init();

    /**
     * <p>
     * Method to set the current <code>Locale</code>
     * </p>
     *
     * @param locale (the <code>Locale</code> to be set as current).
     */
    public void setLocale(Locale locale){
        this.currentLocale = locale;
    }

    /**
     * <p>
     * Method to set this' <code>MyProperty</code>.
     * </p>
     * @param myProperty (this' <code>MyProperty</code>).
     */
    public void setMyProperty(MyProperty myProperty){
        this.myProperty = myProperty;
    }

    /**
     * <p>
     * Method to retrieve a <code>String</code> representation
     * of tis'type.
     * </p>
     *
     * @return (a <code>String</code> representation of this'type).
     */
    public abstract String getViewType();

    public abstract void waitingClear();


}
