package fi.paivola.mapserver.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class SettingsParser {
    
    public List<String> models;
    private final JSONObject obj;

    public SettingsParser() throws IOException, ParseException {
        
        models = new ArrayList<>();

        InputStream is = getClass().getClassLoader().getResourceAsStream("settings.json");
        StringWriter writer = new StringWriter();
        IOUtils.copy(is, writer, "UTF-8");
        obj = (JSONObject) (new JSONParser().parse(writer.toString()));

    }
    
    public Map<String, Object> getModels() {
        Map<String, Object> map = new HashMap<>();
        
        // array of models that we totally want to use
        JSONArray msg = (JSONArray) obj.get("models");
        Iterator<JSONObject> iterator = msg.iterator();
        while (iterator.hasNext()) {
            JSONObject ob = iterator.next();
            System.out.println(ob.get("name").toString());
            try {
                map.put(ob.get("name").toString(), Class.forName(ob.get("class").toString()));
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(SettingsParser.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return map;
    }

}
