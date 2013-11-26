package fi.paivola.mapserver.models;

import fi.paivola.mapserver.utils.Color;
import fi.paivola.mapserver.core.DataFrame;
import fi.paivola.mapserver.core.Event;
import fi.paivola.mapserver.core.Model;
import fi.paivola.mapserver.core.PointModel;
import fi.paivola.mapserver.core.Setting;

public class Asd extends PointModel {

    public int boomcount;

    public Asd(int id) {
        super(id);
        this.settings.add(new Setting("Heepo", "double", "0.1"));
        this.settings.add(new Setting("Heepo2", "int", "45"));
        this.icon = "town";
        this.color = new Color(255, 128, 64);
        this.boomcount = 0;
    }

    @Override
    public void onTick(DataFrame last, DataFrame current) {
        this.addEventToAll(current, new Event("boom", "integer", "9001"));
    }

    @Override
    public void onEvent(Event e) {
        switch (e.name) {
            case "boom":
                this.boomcount++;
                break;
            default:
                break;
        }
    }

}
