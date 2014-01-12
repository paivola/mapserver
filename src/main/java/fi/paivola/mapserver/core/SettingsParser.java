package fi.paivola.mapserver.core;

import fi.paivola.mapserver.utils.CCs;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Parses a JSON file for all settings.
 *
 * @author Juhani
 */
public class SettingsParser {

    public static JSONObject settings = null;

    public static void parse() throws IOException, ParseException {
        parse(null);
    }

    public static void parse(InputStream is) throws IOException, ParseException {

        if (is == null) {
            is = SettingsParser.class.getClassLoader().getResourceAsStream("settings.json");
        }
        
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        settings = (JSONObject) (new JSONParser().parse( s.hasNext() ? s.next() : ""));

    }

    public static Map<String, CCs> getModels() {
        Map<String, CCs> map = new HashMap<>();

        // array of models that we totally want to use
        JSONArray msg = (JSONArray) settings.get("models");
        Iterator<JSONObject> iterator = msg.iterator();
        while (iterator.hasNext()) {
            JSONObject ob = iterator.next();
            try {
                map.put(ob.get("name").toString(), new CCs(Class.forName(ob.get("class").toString())));
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(SettingsParser.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return map;
    }

}
