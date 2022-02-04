package function.dataset;

import function.DatasetFunction;
import org.sp.etl.common.model.Id;

public abstract class DatasetRegisterAsTableFunction extends DatasetFunction {

    private String datasetName;

    public DatasetRegisterAsTableFunction(Id id, String name, String description, boolean isActive, String datasetName) {
        super(id, name, description, isActive);
        this.datasetName = datasetName;
    }

    public String getDatasetName() {
        return datasetName;
    }
}
