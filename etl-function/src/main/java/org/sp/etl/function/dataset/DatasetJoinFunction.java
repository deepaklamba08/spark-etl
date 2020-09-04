package org.sp.etl.function.dataset;

import org.sp.etl.function.DatasetFunction;

public abstract class DatasetJoinFunction extends DatasetFunction {

    private String leftDatasetName;
    private String rightDatasetName;

    private String leftDatasetColumn;
    private String rightDatasetColumn;

    public abstract String joinType();


    public String getLeftDatasetName() {
        return leftDatasetName;
    }

    public void setLeftDatasetName(String leftDatasetName) {
        this.leftDatasetName = leftDatasetName;
    }

    public String getRightDatasetName() {
        return rightDatasetName;
    }

    public void setRightDatasetName(String rightDatasetName) {
        this.rightDatasetName = rightDatasetName;
    }

    public String getLeftDatasetColumn() {
        return leftDatasetColumn;
    }

    public void setLeftDatasetColumn(String leftDatasetColumn) {
        this.leftDatasetColumn = leftDatasetColumn;
    }

    public String getRightDatasetColumn() {
        return rightDatasetColumn;
    }

    public void setRightDatasetColumn(String rightDatasetColumn) {
        this.rightDatasetColumn = rightDatasetColumn;
    }
}
