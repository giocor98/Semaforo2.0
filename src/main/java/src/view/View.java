package src.view;

import src.utils.properties.MyProperty;

import java.util.List;
import java.util.Locale;

/**
 * Interface working as a facade for the src.view package.
 */
public interface View {

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
    public String selectPort(List<String> portList, String defaultPort);

    /**
     * <p>
     * Method called to display an error message to the user.
     * </p>
     *
     * @param errorMessage (the error message to be displayed).
     */
    public void error(String errorMessage);

    /**
     * <p>
     * Method to set the current <code>Locale</code>
     * </p>
     *
     * @param locale (the <code>Locale</code> to be set as current).
     */
    public void setLocale(Locale locale);

    /**
     * <p>
     * Method to set this' <code>MyProperty</code>.
     * </p>
     * @param myProperty (this' <code>MyProperty</code>).
     */
    public void setMyProperty(MyProperty myProperty);

    /**
     * <p>
     * Method to retrieve a <code>String</code> representation
     * of tis'type.
     * </p>
     *
     * @return (a <code>String</code> representation of this'type).
     */
    public String getViewType();
}
