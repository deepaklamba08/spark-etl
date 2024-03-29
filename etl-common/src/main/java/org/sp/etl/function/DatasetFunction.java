package org.sp.etl.function;

import org.sp.etl.common.model.Id;

public abstract class DatasetFunction implements EtlFunction {
    private Id id;
    private String name;
    private String description;
    private boolean isActive;

    public DatasetFunction(Id id, String name, String description, boolean isActive) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.isActive = isActive;
    }

    @Override
    public boolean isActive() {
        return this.isActive;
    }

    @Override
    public Id getId() {
        return this.id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getDescription() {
        return this.description;
    }
}
