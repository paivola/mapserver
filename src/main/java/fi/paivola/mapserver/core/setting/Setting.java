package fi.paivola.mapserver.core.setting;


/**
 * Abstract base class for settings, they are used for getting data from the UI back to the models.
 *
 * @author Juhani
 */
public abstract class Setting {

    public String type;
    public String name;
    public boolean ready;

    public Setting(String name) {
        this.name = name;
        this.ready = false;
    }
    
    public abstract Setting setValue(String value);
    public abstract String getValue();
    
    public abstract Setting setDefault(String def);
    
    /**
     * Formats a setting to a JSON string.
     * 
     * @return  String full of JSON
     */
    @Override
    public abstract String toString();

}
