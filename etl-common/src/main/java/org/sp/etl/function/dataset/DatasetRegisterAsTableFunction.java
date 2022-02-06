package org.sp.etl.function.dataset;

import org.sp.etl.function.DatasetFunction;
import org.sp.etl.common.model.Id;

public class DatasetRegisterAsTableFunction extends DatasetFunction {

    private String datasetName;

    public DatasetRegisterAsTableFunction(Id id, String name, String description, boolean isActive, String datasetName) {
        super(id, name, description, isActive);
        this.datasetName = datasetName;
    }

    public String getDatasetName() {
        return datasetName;
    }
}
