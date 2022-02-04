package function.column;

import org.sp.etl.common.model.Id;

public class DropColumnFunction extends ColumnFunction {

    private String columnName;

    public DropColumnFunction(Id id, String name, String description, boolean isActive, String columnName) {
        super(id, name, description, isActive);
        this.columnName = columnName;
    }

    public String getColumnName() {
        return columnName;
    }


}
