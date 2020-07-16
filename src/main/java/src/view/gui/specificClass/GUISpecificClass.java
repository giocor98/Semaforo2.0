package src.view.gui.specificClass;

import src.utils.properties.MyProperty;

import java.util.Locale;

public abstract class GUISpecificClass {

    protected Locale currentLocale;

    protected MyProperty myProperty;

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
}
