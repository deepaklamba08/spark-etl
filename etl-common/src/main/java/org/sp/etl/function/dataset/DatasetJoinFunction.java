package org.sp.etl.function.dataset;

import org.sp.etl.function.DatasetFunction;
import org.sp.etl.common.model.Id;

public abstract class DatasetJoinFunction extends DatasetFunction {

    private String leftDatasetName;
    private String rightDatasetName;

    private String leftDatasetColumn;
    private String rightDatasetColumn;

    public abstract String joinType();

    public DatasetJoinFunction(Id id, String name, String description, boolean isActive, String leftDatasetName, String rightDatasetName, String leftDatasetColumn, String rightDatasetColumn) {
        super(id, name, description, isActive);
        this.leftDatasetName = leftDatasetName;
        this.rightDatasetName = rightDatasetName;
        this.leftDatasetColumn = leftDatasetColumn;
        this.rightDatasetColumn = rightDatasetColumn;
    }

    public String getLeftDatasetName() {
        return leftDatasetName;
    }

    public String getRightDatasetName() {
        return rightDatasetName;
    }

    public String getLeftDatasetColumn() {
        return leftDatasetColumn;
    }

    public String getRightDatasetColumn() {
        return rightDatasetColumn;
    }
}
