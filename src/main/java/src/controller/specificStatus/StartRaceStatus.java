package src.controller.specificStatus;

import src.app.MainApp;
import src.controller.Status;

public class StartRaceStatus implements SpecificStatus  {
    @Override
    public Status exec(MainApp mainApp) {
        return mainApp.getView().selectRaceType();
    }
}
