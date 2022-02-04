package function.column;

import org.sp.etl.common.model.Id;

public class AddConstantValueFunction extends ColumnFunction {

    private String columnName;
    private Object value;

    public AddConstantValueFunction(Id id, String name, String description, boolean isActive, String columnName, Object value) {
        super(id, name, description, isActive);
        this.columnName = columnName;
        this.value = value;
    }

    public String getColumnName() {
        return columnName;
    }

    public Object getValue() {
        return value;
    }
}
