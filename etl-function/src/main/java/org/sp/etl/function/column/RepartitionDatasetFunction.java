package org.sp.etl.function.column;

import java.util.List;

public class RepartitionDatasetFunction extends ColumnFunction {

    private List<String> partitionColumns;
    private int numPartitons;

    public RepartitionDatasetFunction() {
    }

    public RepartitionDatasetFunction(String name, String description, List<String> partitionColumns, int numPartitons) {
        super(name, description);
        this.partitionColumns = partitionColumns;
        this.numPartitons = numPartitons;
    }

    public List<String> getPartitionColumns() {
        return partitionColumns;
    }

    public void setPartitionColumns(List<String> partitionColumns) {
        this.partitionColumns = partitionColumns;
    }

    public int getNumPartitons() {
        return numPartitons;
    }

    public void setNumPartitons(int numPartitons) {
        this.numPartitons = numPartitons;
    }
}
