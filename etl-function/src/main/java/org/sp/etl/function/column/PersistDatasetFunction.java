package org.sp.etl.function.column;

public class PersistDatasetFunction extends ColumnFunction {

    private String persistLevel;

    public PersistDatasetFunction() {
    }

    public PersistDatasetFunction(String name, String description, String persistLevel) {
        super(name, description);
        this.persistLevel = persistLevel;
    }

    public String getPersistLevel() {
        return persistLevel;
    }

    public void setPersistLevel(String persistLevel) {
        this.persistLevel = persistLevel;
    }
}
