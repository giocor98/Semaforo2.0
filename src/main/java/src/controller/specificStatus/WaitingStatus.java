package src.controller.specificStatus;

import src.app.MainApp;
import src.controller.Status;
import src.utils.connection.Connection;
import src.view.View;

public class WaitingStatus implements SpecificStatus {
    @Override
    public Status exec(MainApp mainApp) {

        View view = mainApp.getView();
        Connection connection = mainApp.getConnection();

        //####LOADING
        view.init();

        view.waiting();

        view.setWaiting("Initialisation", 0);

        //##Load connection
        view.setWaiting("portRetrieving", 10);

        if(connection == null){
            Object lock = new Object();
            for(int i=0; i<4; i++){
                synchronized (lock) {
                    try {
                        lock.wait(1000);
                    } catch (InterruptedException ignore) {
                    }
                }
                view.setWaiting((100/4)*(i+1));
            }
        }


        return Status.HOME_PAGE;
    }
}
