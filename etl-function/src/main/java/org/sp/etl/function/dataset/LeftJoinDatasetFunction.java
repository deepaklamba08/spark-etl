package org.sp.etl.function.dataset;

public class LeftJoinDatasetFunction extends DatasetJoinFunction {
    @Override
    public String joinType() {
        return "left";
    }
}
