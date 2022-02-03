package org.sp.etl.common.model;

import java.util.Objects;

public class StringId implements Id {
    private String value;

    public StringId(String value) {
        this.value = value;
    }

    @Override
    public String getStringValue() {
        return this.value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StringId stringId = (StringId) o;
        return Objects.equals(value, stringId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
