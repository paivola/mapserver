package fi.paivola.mapserver.core;

import fi.paivola.mapserver.core.setting.SettingMaster;
import fi.paivola.mapserver.utils.LatLng;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Meet WSServer, our communications manager.
 * @author juhani
 */
public class WSServer extends WebSocketServer {
    
    private final JSONParser parser = new JSONParser();
    private final Map<String, GameThread> threads;
    private final static Logger log = Logger.getLogger("mapserver");

    public WSServer(int port) throws UnknownHostException {
        super(new InetSocketAddress(port));
        threads = new HashMap<>();
        log.log(Level.INFO, "created server on port: {0}", port);
    }

    public WSServer(InetSocketAddress address) {
        super(address);
        threads = new HashMap<>();
        log.log(Level.INFO, "created server on port: {0}", address.getPort());
    }

    @Override
    public void onOpen(WebSocket ws, ClientHandshake ch) {
        log.log(Level.INFO, "client connected from {0}", ws.getRemoteSocketAddress().getAddress().toString());
    }

    @Override
    public void onClose(WebSocket ws, int i, String string, boolean bln) {
        log.log(Level.INFO, "client disconnected from {0}", ws.getRemoteSocketAddress().getAddress().toString());
    }

    @Override
    public void onMessage(WebSocket ws, String string) {
        JSONObject obj = null;
        JSONObject responce = new JSONObject();
        
        try {
            obj = (JSONObject)parser.parse(string);
        } catch (ParseException ex) {
            Logger.getLogger(WSServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // check if we actually have a JSONobject and that it contains something for use to do.
        if(obj == null || !obj.containsKey("action")) {
            return;
        }
        
        // since we are responding to the action, just copy it over.
        responce.put("action", obj.get("action"));
        
        // switching the action seems like the best way to do this.
        switch(obj.get("action").toString()) {
            case "create": // creation of a gameManager
                this.callCreate(obj, responce);
                break;
            case "start": // starting the simulation
                this.callStart(obj, responce);
                break;
            case "getsettings": // get all settings from all models
                this.callGetSettings(obj, responce);
                break;
            case "setsettings": // set settings for a model_id
                this.callSetSettings(obj, responce);
                break;
            case "add": // add a model to the gameManager
                this.callAdd(obj, responce);
                break;
            case "move": // set position for a model_id
                this.callMove(obj, responce);
                break;
            case "link": // link two models together
                this.callLink(obj, responce);
                break;
            case "getdata": // gets csv data
                this.callGetdata(obj, responce);
                break;
            case "info": // get information of a model_id
                this.callInfo(obj, responce);
                break;
            case "hello": // just popping in to say hello!
                this.callHello(obj, responce);
                break;
            default: // otherwise there seems to be no match, thats a error
                this.error(responce, "Command not found!");
                break;
        }
        
        // and finally send our responce back
        ws.send(responce.toJSONString());
    }
    
    /**
     * Create a gameManager, returns manager_id.
     * @param in
     * @param out 
     */
    private void callCreate(JSONObject in, JSONObject out) {
        int ticks;
        if(in.containsKey("ticks")) {
            ticks = Integer.parseInt(in.get("ticks").toString());
        }else{
            ticks = Integer.parseInt(SettingsParser.settings.get("default_ticks").toString());
        }
        GameThread newthread = new GameThread(ticks);
        this.threads.put(""+this.threads.size(), newthread);
        out.put("manager_id", ""+(this.threads.size()-1));
        success(out);
    }
    
    /**
     * Starts simulation in a manager_id.
     * @param in
     * @param out 
     */
    private void callStart(JSONObject in, JSONObject out) {
        GameThread gt;
        if((gt = getThread(in, out)) == null)
            return;
        gt.start();
        success(out);
    }
    
    /**
     * Creates a model in manager_id with given settings and optional Longitude, Latitude.
     * @param in
     * @param out 
     */
    private void callAdd(JSONObject in, JSONObject out) {
        GameThread gt;
        if((gt = getThread(in, out)) == null)
            return;
        
        String type = "undefined";
        SettingMaster sm;
        if(in.containsKey("type"))
            type = in.get("type").toString();
        if(in.containsKey("settings"))
            sm = SettingMaster.fromJSON((JSONObject)in.get("settings"));
        else
            return;
        
        Model mod = gt.game.createModel(type, sm);
        
        if(in.containsKey("lat"))
            mod.ll.latitude = Double.parseDouble(in.get("lat").toString());
        if(in.containsKey("lng"))
            mod.ll.longitude = Double.parseDouble(in.get("lng").toString());
        
        gt.game.addModel(mod, type);
        out.put("model_id", mod.id);
        success(out);
    }
    
    /**
     * Moves a model_id to a new Longitude/Latitude.
     * @param in
     * @param out 
     */
    private void callMove(JSONObject in, JSONObject out) {
        GameThread gt;
        if((gt = getThread(in, out)) == null)
            return;
        
        Model mod;
        if((mod = getModel(in, out, gt.game)) == null)
            return;
        
        LatLng ll;
        if((ll = getLatLng(in, out)) == null)
            return;
        
        mod.setLatLng(ll);
        
        success(out);
    }
    
    /**
     * Links two models together.
     * @param in
     * @param out 
     */
    private void callLink(JSONObject in, JSONObject out) {
        GameThread gt;
        if((gt = getThread(in, out)) == null)
            return;
        
        Model mod1;
        Model mod2;
        if((mod1 = getModel1(in, out, gt.game)) == null)
            return;
        if((mod2 = getModel2(in, out, gt.game)) == null)
            return;
        
        gt.game.linkModels(mod1, mod2);
        
        success(out);
    }
    
    /**
     * This does something.
     * @param in
     * @param out 
     */
    private void callInfo(JSONObject in, JSONObject out) {
        GameThread gt;
        if((gt = getThread(in, out)) == null)
            return;
        Model mod;
        if((mod = getModel(in, out, gt.game)) == null)
            return;
        
        out.put("model", mod);
        success(out);
    }
    
    /**
     * Returns CSV data after the simulation is complete from a manager_id.
     * @param in
     * @param out 
     */
    private void callGetdata(JSONObject in, JSONObject out) {
        GameThread gt;
        if((gt = getThread(in, out)) == null)
            return;
        if(!gt.game.ready) {
            error(out, "Run not completed yet");
            return;
        }
        out.put("data", gt.game.getData());
        success(out);
    }
    
    /**
     * Sets the settings of a model_id.
     * @param in
     * @param out 
     */
    private void callSetSettings(JSONObject in, JSONObject out) {
        GameThread gt;
        if((gt = getThread(in, out)) == null)
            return;
        
        Model mod;
        if((mod = getModel(in, out, gt.game)) == null)
            return;
        
        mod.sm = SettingMaster.fromJSON((JSONObject)in.get("settings"));
        mod.addEvent(new Event("settingsChanged", Event.Type.NOTIFICATION, null), mod);
        
        success(out);
    }
    
    /**
     * Gets all settings from all models in a manager_id.
     * @param in
     * @param out 
     */
    private void callGetSettings(JSONObject in, JSONObject out) {
        GameThread gt;
        if((gt = getThread(in, out)) == null)
            return;
        out.put("settings", gt.game.getSettings());
        success(out);
    }
    
    /**
     * Just popping in to say hello!
     * @param in
     * @param out 
     */
    private void callHello(JSONObject in, JSONObject out) {
        success(out);
    }
    
    /**
     * Adds a error to out.
     * @param out JSONObject that is our to-be-serialized message
     * @param message verbose error message for other developers to understand their mistakes
     */
    private void error(JSONObject out, String message) {
        out.put("status", "error");
        out.put("error", message);
    }
    
    /**
     * Adds a status: success to out.
     * @param out JSONObject that is our to-be-serialized message
     */
    private void success(JSONObject out) {
        out.put("status", "success");
    }
    
    /**
     * Gets the thread by manager_id
     * @param in
     * @param out
     * @return GameThread if success, otherwise null
     */
    private GameThread getThread(JSONObject in, JSONObject out) {
        if(!in.containsKey("manager_id") || !this.threads.containsKey(in.get("manager_id").toString())) {
            error(out, "You need to provide a existing manager_id!");
            return null;
        }
        return this.threads.get(in.get("manager_id").toString());
    }
    
    /**
     * Gets the model from GameManager by model_id.
     * @param in
     * @param out
     * @param gm GameManager that has been resolved from getThread
     * @return Model if success, otherwise null
     */
    private Model getModel(JSONObject in, JSONObject out, GameManager gm) {
        if(!in.containsKey("model_id") || !gm.containsModel(Integer.parseInt(in.get("model_id").toString()))) {
            error(out, "You need to provide a existing model_id!");
            return null;
        }
        return gm.getActive(in.get("model_id").toString());
    }
    
    /**
     * Gets the model from GameManager by model1_id.
     * @param in
     * @param out
     * @param gm GameManager that has been resolved from getThread
     * @return Model if success, otherwise null
     */
    private Model getModel1(JSONObject in, JSONObject out, GameManager gm) {
        if(!in.containsKey("model1_id") || !gm.containsModel(Integer.parseInt(in.get("model1_id").toString()))) {
            error(out, "You need to provide a existing model1_id!");
            return null;
        }
        return gm.getActive(in.get("model_id1").toString());
    }
    
    /**
     * Gets the model from GameManager by model2_id.
     * @param in
     * @param out
     * @param gm GameManager that has been resolved from getThread
     * @return Model if success, otherwise null
     */
    private Model getModel2(JSONObject in, JSONObject out, GameManager gm) {
        if(!in.containsKey("model2_id") || !gm.containsModel(Integer.parseInt(in.get("model2_id").toString()))) {
            error(out, "You need to provide a existing model2_id!");
            return null;
        }
        return gm.getActive(in.get("model_id2").toString());
    }
    
    /**
     * Gets a LatLng object from a JSONObject.
     * @param in
     * @param out
     * @return LatLng if success, otherwise null
     */
    private LatLng getLatLng(JSONObject in, JSONObject out) {
        if(!in.containsKey("lat") || !in.containsKey("lng")) {
            error(out, "You need to provice lat and lng");
            return null;
        }
        return new LatLng(Double.parseDouble(in.get("lat").toString()), Double.parseDouble(in.get("lng").toString()));
    }

    @Override
    public void onError(WebSocket ws, Exception excptn) {

    }

}
