package src.controller.specificStatus;

import src.app.MainApp;
import src.controller.Status;
import src.utils.connection.Connection;
import src.view.View;

public class HomePageStatus implements SpecificStatus{
    @Override
    public Status exec(MainApp mainApp) {
        View view = mainApp.getView();
        Connection connection = mainApp.getConnection();

        Status ret = view.homePage(connection);

        if(ret == Status.HOME_PAGE){
            view.error("closePortAction", new String[]{});
        }

        return ret;
    }
}
