package org.sp.etl.function;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.sp.etl.function.column.*;
import org.sp.etl.function.dataset.InnerJoinDatasetFunction;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXTERNAL_PROPERTY, property = "type")

@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = ColumnFunction.class, name = "columnFunction"),
                @JsonSubTypes.Type(value = RenameColumnFunction.class, name = "renameColumnFunction"),
                @JsonSubTypes.Type(value = AddConstantValueFunction.class, name = "addConstantValueFunction"),
                @JsonSubTypes.Type(value = DropColumnColumnFunction.class, name = "dropColumnColumnFunction"),
                @JsonSubTypes.Type(value = DatasetFunction.class, name = "datasetFunction"),
                @JsonSubTypes.Type(value = InnerJoinDatasetFunction.class, name = "innerJoinDatasetFunction"),
                @JsonSubTypes.Type(value = RepartitionDatasetFunction.class, name = "repartitionDatasetFunction")
        })

public interface EtlFunction {

    public String name();

    public String description();
}
