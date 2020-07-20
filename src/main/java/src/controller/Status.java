package src.controller;

import src.app.MainApp;
import src.controller.specificStatus.SpecificStatus;
import src.controller.specificStatus.WaitingStatus;
import src.view.View;

public enum Status {
    WAITING(new WaitingStatus()),
    HOME_PAGE(null),
    SETTINGS(null),
    START_RACE(null),
    START(null),
    CLOSE(null);

    private SpecificStatus specificStatus;

    private Status(SpecificStatus specificStatus){
        this.specificStatus = specificStatus;
    }

    public Status exec(MainApp mainApp){
        return this.specificStatus.exec(mainApp);
    }
}
