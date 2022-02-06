package org.sp.etl.function.dataset;

import org.sp.etl.function.DatasetFunction;
import org.sp.etl.common.model.Id;

public class DatasetUnionFunction extends DatasetFunction {

    private String secondDatasetName;
    private boolean unionByName;

    public DatasetUnionFunction(Id id, String name, String description, boolean isActive, String secondDatasetName, boolean unionByName) {
        super(id, name, description, isActive);
        this.secondDatasetName = secondDatasetName;
        this.unionByName = unionByName;
    }

    public String getSecondDatasetName() {
        return secondDatasetName;
    }

    public boolean isUnionByName() {
        return unionByName;
    }
}
