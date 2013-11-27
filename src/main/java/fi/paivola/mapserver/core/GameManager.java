package fi.paivola.mapserver.core;

import fi.paivola.mapserver.utils.CCs;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.parser.ParseException;

public class GameManager {

    public int tick_amount;
    public int tick_current;
    private Map<String, CCs> models;
    private final List<DataFrame> frames;
    private final List<Model> active_models;
    private SettingsParser sp;
    public int current_id;

    public GameManager(int tick_amount) {
        this.tick_amount = tick_amount;
        this.tick_current = 0;
        this.frames = new ArrayList<>();
        this.active_models = new ArrayList<>();
        this.current_id = 0;

        try {
            this.sp = new SettingsParser();
            this.models = this.sp.getModels();
        } catch (IOException | ParseException ex) {
            Logger.getLogger(GameManager.class.getName())
                    .log(Level.SEVERE, null, ex);
        }

        clearFrames();
        runRegisterations();
    }

    private void runRegisterations() {
        for (Map.Entry pair : this.models.entrySet()) {
            Class cls;
            cls = (Class) ((CCs) pair.getValue()).cls;
            Constructor<Model> c;
            try {
                c = cls.getDeclaredConstructor(int.class);
                c.setAccessible(true);
                Model m;
                try {
                    m = c.newInstance(0);
                    m.onRegisteration(this);
                } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                    Logger.getLogger(GameManager.class.getName())
                            .log(Level.SEVERE, null, ex);
                }
            } catch (NoSuchMethodException | SecurityException ex) {
                Logger.getLogger(GameManager.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
        }
    }

    private void clearFrames() {
        frames.clear();
        for (int i = 0; i < this.tick_amount; i++) {
            frames.add(new DataFrame(i));
        }
    }

    public boolean addModel(Model m, String type) {
        m.addExtensions(this, models.get(type).clss);
        return this.active_models.add(m);
    }

    public Model createModel(String type) {
        Class cls;
        cls = (Class) models.get(type).cls;
        Model m = null;
        try {
            Constructor<Model> c;
            c = cls.getDeclaredConstructor(int.class);
            c.setAccessible(true);
            try {
                m = c.newInstance(this.current_id++);
            } catch (InstantiationException | IllegalAccessException |
                    IllegalArgumentException | InvocationTargetException ex) {
                Logger.getLogger(GameManager.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
        } catch (NoSuchMethodException | SecurityException ex) {
            Logger.getLogger(GameManager.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
        return m;
    }

    /**
     * Links two models together.
     *
     * @param first first model
     * @param second second model
     * @return returns true if successful, false otherwise
     */
    public boolean linkModels(Model first, Model second) {
        if (!first.linkModel(second)) {
            return false;
        }
        if (!second.linkModel(first)) {
            first.delinkModel(second);
            return false;
        }

        return true;
    }

    public int populateDefaults(Model m, DataFrame df) {

        System.out.println("Init defaults for " + m.id);

        //this.sp.
        return 0;
    }

    public int stepTrough() {

        while (this.tick_current < this.tick_amount) {
            this.step();
        }

        return 0;
    }

    public int step() {

        System.out.println(" ---[ STEP " + String.format("%5d", this.tick_current) + " ]--- ");

        DataFrame current = this.frames.get(this.tick_current);
        if (this.tick_current > 0) {
            DataFrame last = this.frames.get(this.tick_current - 1);
            for (int i = 0; i < this.active_models.size(); i++) {
                this.active_models.get(i).onTickStart(last, current);
            }
        } else { //step 0 needs to use default values
            for (int i = 0; i < this.active_models.size(); i++) {
                this.populateDefaults(this.active_models.get(i), current);
            }
        }

        this.tick_current++;

        return 0;
    }

    /**
     * Adds a extender that will be added to each subsequent target model.
     *
     * @param towhere the models name where to add
     * @param name name of the extender
     * @param cls the extending class
     */
    public void registerExtension(String towhere, String name, Object cls) {
        this.models.get(towhere).clss.put(name, cls);
    }

}
