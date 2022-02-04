package org.sp.etl.function.dataset;

import org.sp.etl.function.DatasetFunction;
import org.sp.etl.common.model.Id;

public class UnPersistDatasetFunction extends DatasetFunction {


    public UnPersistDatasetFunction(Id id, String name, String description, boolean isActive) {
        super(id, name, description, isActive);
    }
}
