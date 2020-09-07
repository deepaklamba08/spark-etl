package org.sp.etl.function.dataset;

public class RightJoinDatasetFunction extends DatasetJoinFunction {
    @Override
    public String joinType() {
        return "right";
    }
}
