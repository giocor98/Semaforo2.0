package src.view;

import java.util.List;

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
     * </p>
     *
     * @param portList (the list of port available to the choice).
     * @return (the user selected port).
     */
    public String selectPort(List<String> portList);

    /**
     * <p>
     * Method called to display an error message to the user.
     * </p>
     *
     * @param errorMessage (the error message to be displayed).
     */
    public void error(String errorMessage);
}
