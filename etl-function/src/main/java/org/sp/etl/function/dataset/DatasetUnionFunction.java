package org.sp.etl.function.dataset;

import org.sp.etl.function.DatasetFunction;

public abstract class DatasetUnionFunction extends DatasetFunction {

    private String secondDatasetName;
    private boolean unionByName;

    public DatasetUnionFunction() {
    }

    public DatasetUnionFunction(String secondDatasetName, boolean unionByName) {
        this.secondDatasetName = secondDatasetName;
        this.unionByName = unionByName;
    }

    public String getSecondDatasetName() {
        return secondDatasetName;
    }

    public void setSecondDatasetName(String secondDatasetName) {
        this.secondDatasetName = secondDatasetName;
    }

    public boolean isUnionByName() {
        return unionByName;
    }

    public void setUnionByName(boolean unionByName) {
        this.unionByName = unionByName;
    }
}
