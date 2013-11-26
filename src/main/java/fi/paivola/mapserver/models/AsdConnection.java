package fi.paivola.mapserver.models;

import fi.paivola.mapserver.core.ConnectionModel;
import fi.paivola.mapserver.core.DataFrame;
import fi.paivola.mapserver.core.Event;
import fi.paivola.mapserver.core.GameManager;

public class AsdConnection extends ConnectionModel {

    public AsdConnection(int id) {
        super(id);
        this.passtrough = true;
    }

    @Override
    public void onTick(DataFrame last, DataFrame current) {

    }

    @Override
    public void onEvent(Event e) {

    }

    @Override
    public void onRegisteration(GameManager gm) {

    }

}
