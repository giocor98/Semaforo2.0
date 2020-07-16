package src.view.gui;

import src.utils.properties.MyProperty;
import src.view.View;

import java.util.List;
import java.util.Locale;

// TODO: 15/07/20 comment it 

public class GUI implements View {
    @Override
    public String selectPort(List<String> portList, String defaultPort) {
        return null;
    }

    @Override
    public void error(String errorMessage) {

    }

    @Override
    public void setLocale(Locale locale) {

    }

    @Override
    public void setMyProperty(MyProperty myProperty){

    }

    @Override
    public String getViewType() {
        return "GUI";
    }
}
