package org.sp.etl.common.model;

import org.sp.etl.common.repo.RepositoryType;

public enum ConfigurationType {
    JSON("json");

    private String typeName;

    ConfigurationType(String typeName) {
        this.typeName = typeName;
    }

    public static ConfigurationType getConfigurationType(String typeName) {
        for (ConfigurationType repositoryType : ConfigurationType.values()) {
            if (repositoryType.typeName.equals(typeName)) {
                return repositoryType;
            }
        }
        throw new IllegalArgumentException("invalid configuration type - " + typeName);
    }
}