package org.sp.etl.function;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.sp.etl.function.column.*;
import org.sp.etl.function.column.agg.GroupByDatasetFunction;
import org.sp.etl.function.dataset.*;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXTERNAL_PROPERTY, property = "type")

@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = ColumnFunction.class, name = "columnFunction"),
                @JsonSubTypes.Type(value = RenameColumnFunction.class, name = "renameColumnFunction"),
                @JsonSubTypes.Type(value = AddConstantValueFunction.class, name = "addConstantValueFunction"),
                @JsonSubTypes.Type(value = DropColumnColumnFunction.class, name = "dropColumnColumnFunction"),
                @JsonSubTypes.Type(value = DatasetFunction.class, name = "datasetFunction"),
                @JsonSubTypes.Type(value = InnerJoinDatasetFunction.class, name = "innerJoinDatasetFunction"),
                @JsonSubTypes.Type(value = RepartitionDatasetFunction.class, name = "repartitionDatasetFunction"),
                @JsonSubTypes.Type(value = PersistDatasetFunction.class, name = "persistDatasetFunction"),
                @JsonSubTypes.Type(value = UnPersistDatasetFunction.class, name = "unPersistDatasetFunction"),
                @JsonSubTypes.Type(value = GroupByDatasetFunction.class, name = "groupByDatasetFunction"),
                @JsonSubTypes.Type(value = DatasetUnionFunction.class, name = "datasetUnionFunction"),
                @JsonSubTypes.Type(value = DatasetRegisterAsTableFunction.class, name = "datasetRegisterAsTableFunction"),
                @JsonSubTypes.Type(value = FilterDatasetFunction.class, name = "filterDatasetFunction"),
                @JsonSubTypes.Type(value = SortDatasetFunction.class, name = "sortDatasetFunction"),
                @JsonSubTypes.Type(value = LeftJoinDatasetFunction.class, name = "leftJoinDatasetFunction"),
                @JsonSubTypes.Type(value = RightJoinDatasetFunction.class, name = "rightJoinDatasetFunction"),
                @JsonSubTypes.Type(value = DateAndTimeFunction.CurrentDateFunction.class, name = "currentDateFunction"),
                @JsonSubTypes.Type(value = DateAndTimeFunction.CurrentTimestampFunction.class, name = "currentTimestampFunction")
        })

public interface EtlFunction {

    public String name();

    public String description();
}
