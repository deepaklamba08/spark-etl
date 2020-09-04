package org.sp.etl.function.dataset;

public class InnerJoinDatasetFunction extends DatasetJoinFunction {
    @Override
    public String joinType() {
        return "inner";
    }
}
