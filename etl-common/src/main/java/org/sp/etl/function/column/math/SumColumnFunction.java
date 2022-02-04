package org.sp.etl.function.column.math;

import org.sp.etl.common.model.Id;
import org.sp.etl.function.column.ColumnFunction;

import java.util.List;

public class SumColumnFunction extends ColumnFunction {

    private String resultColumnName;
    private List<String> columns;

    public SumColumnFunction(Id id, String name, String description, boolean isActive, String resultColumnName, List<String> columns) {
        super(id, name, description, isActive);
        this.resultColumnName = resultColumnName;
        this.columns = columns;
    }

    public String getResultColumnName() {
        return resultColumnName;
    }

    public List<String> getColumns() {
        return columns;
    }
}
