package fi.paivola.mapserver.core;

/**
 * The base class for extension models.
 *
 * @author Juhani
 */
public abstract class ExtensionModel extends Model {

    public ExtensionModel(int id) {
        super(id);
        this.type = "extension";
    }

    @Override
    public void onTick(DataFrame last, DataFrame current) {

    }

    /**
     * A special onTick for extension models. Ran after master finishes.
     *
     * @param parent parent model
     * @param last last dataframe
     * @param current current dataframe
     */
    abstract void onExtensionTick(Model parent,
            DataFrame last, DataFrame current);

}
