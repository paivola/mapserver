package fi.paivola.mapserver.models;

import au.com.bytecode.opencsv.CSVReader;
import fi.paivola.mapserver.core.DataFrame;
import fi.paivola.mapserver.core.Event;
import fi.paivola.mapserver.core.GameManager;
import fi.paivola.mapserver.core.GlobalModel;
import fi.paivola.mapserver.core.setting.SettingInt;
import fi.paivola.mapserver.core.setting.SettingMaster;
import fi.paivola.mapserver.utils.RangeInt;
import java.io.IOException;
import java.io.InputStreamReader;
import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Example global model READ THIS.
 *
 * @author juhani
 */
public class ExampleGlobal extends GlobalModel {

    private int cats = 0;
    private final double[] activity;

    public ExampleGlobal(int id) {
        super(id);
        activity = new double[53];

        // Example of how to parse a CSV file
        try {
            CSVReader reader = new CSVReader(new InputStreamReader(ExampleGlobal.class.getClassLoader().getResourceAsStream("cats.csv")));
            String[] nextLine;
            int line = 0;
            while ((nextLine = reader.readNext()) != null) {
                if (line > 0) {
                    activity[parseInt(nextLine[0]) - 1] = parseDouble(nextLine[1]);
                }
                line++;
            }
        } catch (IOException ex) {
            Logger.getLogger(ExampleGlobal.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public int getCats(int week) {
        // activity * cats
        return (int) Math.round(activity[week % 52] * cats);
    }

    @Override
    public void onTick(DataFrame last, DataFrame current) {
        // This right here saves a global piece of data. Others can get it by using getGlobalData.
        current.saveGlobalData("cats", getCats(current.index));
    }

    @Override
    public void onEvent(Event e, DataFrame current) {

    }

    @Override
    public void onRegisteration(GameManager gm, SettingMaster sm) {
        sm.name = "exampleGlobal";
        sm.settings.put("cats", new SettingInt("How many cats are there?", 1, new RangeInt(0, 333)));
    }

    @Override
    public void onGenerateDefaults(DataFrame df) {
        df.saveGlobalData("cats", getCats(df.index));
    }

    @Override
    public void onUpdateSettings(SettingMaster sm) {
        cats = parseInt(sm.settings.get("cats").getValue());
    }

}
