package org.sp.etl.function;

public abstract class DatasetFunction implements EtlFunction {
    private String name;
    private String description;

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public String description() {
        return this.description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
