package org.sp.etl.function;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.sp.etl.function.column.AddConstantValueFunction;
import org.sp.etl.function.column.ColumnFunction;
import org.sp.etl.function.column.DropColumnColumnFunction;
import org.sp.etl.function.column.RenameColumnFunction;
import org.sp.etl.function.dataset.InnerJoinDatasetFunction;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXTERNAL_PROPERTY, property = "type")

@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = ColumnFunction.class, name = "columnFunction"),
                @JsonSubTypes.Type(value = RenameColumnFunction.class, name = "renameColumnFunction"),
                @JsonSubTypes.Type(value = AddConstantValueFunction.class, name = "addConstantValueFunction"),
                @JsonSubTypes.Type(value = DropColumnColumnFunction.class, name = "dropColumnColumnFunction"),
                @JsonSubTypes.Type(value = DatasetFunction.class, name = "datasetFunction"),
                @JsonSubTypes.Type(value = InnerJoinDatasetFunction.class, name = "innerJoinDatasetFunction")
        })

public interface EtlFunction {

    public String name();

    public String description();
}
