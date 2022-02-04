package function.dataset;

import function.DatasetFunction;
import function.column.ColumnFunction;
import org.sp.etl.common.model.Id;

public class UnPersistDatasetFunction extends DatasetFunction {


    public UnPersistDatasetFunction(Id id, String name, String description, boolean isActive) {
        super(id, name, description, isActive);
    }
}
