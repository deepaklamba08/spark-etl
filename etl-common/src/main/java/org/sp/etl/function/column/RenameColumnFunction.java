package org.sp.etl.function.column;

import org.sp.etl.common.model.Id;

public class RenameColumnFunction extends ColumnFunction {

    private String oldName;
    private String newName;

    public RenameColumnFunction(Id id, String name, String description, boolean isActive, String oldName, String newName) {
        super(id, name, description, isActive);
        this.oldName = oldName;
        this.newName = newName;
    }

    public String getOldName() {
        return oldName;
    }


    public String getNewName() {
        return newName;
    }


}
