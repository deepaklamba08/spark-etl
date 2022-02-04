package org.sp.etl.common.repo;

public enum RepositoryType {
    FileSystem("localFs"), Jdbc("jdbc");

    private String typeName;

    RepositoryType(String typeName) {
        this.typeName = typeName;
    }

    public static RepositoryType getRepositoryType(String typeName) {
        for (RepositoryType repositoryType : RepositoryType.values()) {
            if (repositoryType.typeName.equals(typeName)) {
                return repositoryType;
            }
        }
        throw new IllegalArgumentException("invalid repository type - " + typeName);
    }
}


