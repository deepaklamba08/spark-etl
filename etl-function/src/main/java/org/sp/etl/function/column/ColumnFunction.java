package org.sp.etl.function.column;

import org.sp.etl.function.EtlFunction;

public abstract class ColumnFunction implements EtlFunction {

    private String name;
    private String description;

    public ColumnFunction(){}

    public ColumnFunction(String name, String description) {
        this.name = name;
        this.description = description;
    }

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
