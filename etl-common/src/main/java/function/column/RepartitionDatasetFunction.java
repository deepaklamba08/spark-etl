package function.column;

import org.sp.etl.common.model.Id;

import java.util.List;

public class RepartitionDatasetFunction extends ColumnFunction {

    private List<String> partitionColumns;
    private int numPartitions;

    public RepartitionDatasetFunction(Id id, String name, String description, boolean isActive, List<String> partitionColumns, int numPartitions) {
        super(id, name, description, isActive);
        this.partitionColumns = partitionColumns;
        this.numPartitions = numPartitions;
    }
}
