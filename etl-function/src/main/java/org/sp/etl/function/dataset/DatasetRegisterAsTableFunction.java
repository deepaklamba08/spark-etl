package org.sp.etl.function.dataset;

import org.sp.etl.function.DatasetFunction;

public abstract class DatasetRegisterAsTableFunction extends DatasetFunction {

    private String datasetName;

    public DatasetRegisterAsTableFunction(){}

    public DatasetRegisterAsTableFunction(String datasetName) {
        this.datasetName = datasetName;
    }

    public String getDatasetName() {
        return datasetName;
    }
}
