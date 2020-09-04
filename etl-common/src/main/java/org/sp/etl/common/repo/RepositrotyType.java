package org.sp.etl.common.repo;

public enum RepositrotyType {
    LocalFileSystem("localFs"), HdfsFileSystem("hdfs"), Jdbc("jdbc");

    private String typeName;

    RepositrotyType(String typeName) {
        this.typeName = typeName;
    }

    public static RepositrotyType getRepositrotyType(String typeName) {
        for (RepositrotyType repositrotyType : RepositrotyType.values()) {
            if (repositrotyType.typeName.equals(typeName)) {
                return repositrotyType;
            }
        }
        throw new IllegalArgumentException("invalid repository type - " + typeName);
    }
}


