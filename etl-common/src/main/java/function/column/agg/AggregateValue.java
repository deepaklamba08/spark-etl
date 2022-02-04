package function.column.agg;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXTERNAL_PROPERTY, property = "type")

@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = MaxValue.class, name = "maxValue"),
                @JsonSubTypes.Type(value = SumValue.class, name = "sumValue")
        })

public abstract class AggregateValue {

    private String columnName;
    private String resultAlias;

    public AggregateValue() {
    }

    public AggregateValue(String columnName, String resultAlias) {
        this.columnName = columnName;
        this.resultAlias = resultAlias;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getResultAlias() {
        return resultAlias;
    }

    public void setResultAlias(String resultAlias) {
        this.resultAlias = resultAlias;
    }
}
