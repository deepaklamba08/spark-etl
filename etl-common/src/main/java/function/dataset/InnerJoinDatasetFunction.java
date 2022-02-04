package function.dataset;

import org.sp.etl.common.model.Id;

public class InnerJoinDatasetFunction extends DatasetJoinFunction {
    public InnerJoinDatasetFunction(Id id, String name, String description, boolean isActive, String leftDatasetName, String rightDatasetName, String leftDatasetColumn, String rightDatasetColumn) {
        super(id, name, description, isActive, leftDatasetName, rightDatasetName, leftDatasetColumn, rightDatasetColumn);
    }

    @Override
    public String joinType() {
        return "inner";
    }
}
