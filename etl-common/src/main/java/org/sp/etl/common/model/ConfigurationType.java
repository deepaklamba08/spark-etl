package org.sp.etl.common.model;

public enum ConfigurationType {
    JSON("json");

    private String typeName;

    ConfigurationType(String typeName) {
        this.typeName = typeName;
    }

    public static ConfigurationType getConfigurationType(String typeName) {
        for (ConfigurationType configurationType : ConfigurationType.values()) {
            if (configurationType.typeName.equals(typeName)) {
                return configurationType;
            }
        }
        throw new IllegalArgumentException("invalid configuration type - " + typeName);
    }

    public String getTypeName() {
        return typeName;
    }
}
