package function.column;

import org.sp.etl.common.model.Id;

public class PersistDatasetFunction extends ColumnFunction {

    private String persistLevel;

    public PersistDatasetFunction(Id id, String name, String description, boolean isActive, String persistLevel) {
        super(id, name, description, isActive);
        this.persistLevel = persistLevel;
    }

    public String getPersistLevel() {
        return persistLevel;
    }


}
