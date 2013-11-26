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
                    m = c.newInstance(this.current_id++);
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

    public int createModel(String type) {
        Class cls;
        cls = (Class) models.get(type).cls;
        if (cls == null) {
            return 1;
        }
        try {
            Constructor<Model> c;
            c = cls.getDeclaredConstructor(int.class);
            c.setAccessible(true);
            try {
                this.active_models.add(c.newInstance(this.current_id++));
                this.active_models.get(this.active_models.size() - 1)
                        .addExtensions(this, models.get(type).clss);
            } catch (InstantiationException | IllegalAccessException |
                    IllegalArgumentException | InvocationTargetException ex) {
                Logger.getLogger(GameManager.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
        } catch (NoSuchMethodException | SecurityException ex) {
            Logger.getLogger(GameManager.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public int populateDefaults(Model m, DataFrame df) {

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

        DataFrame current = this.frames.get(this.tick_current);
        if (this.tick_current > 0) {
            DataFrame last = this.frames.get(this.tick_current - 1);
            for (int i = 0; i < this.active_models.size(); i++) {
                this.active_models.get(i).onTick(last, current);
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
