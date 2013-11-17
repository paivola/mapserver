package fi.paivola.mapserver.core;

public abstract class Model {
    
    public enum Type {
        POINT, CONNECTION;
    }
    
    public long id;
    
    public Model(long id) {
        this.id = id;
    }
    
    public abstract MapObjectSetting[] getSettings();
    public abstract String[] getAllowedTags();
    public abstract String[] getTags();
    public abstract Type getType();
    public abstract int onTick(DataFrame df);
    public abstract int onInit();
    
}

/*

    public class Asd extends Model {

        public Asd(int moi) {
            super();
            this.moi = moi;
        }

        @override
        public int onTick() {

        }

    }

*/