package org.sp.etl.common.ds;

import java.io.File;

public class LocalFileSystemDataSource extends FileSystemDataSource {
    public LocalFileSystemDataSource() {
    }

    public LocalFileSystemDataSource(String baseDirectory, String name) {
        super(baseDirectory, name);
    }

    @Override
    public String getPath(String fileName) {
        return new File(this.getBaseDirectory(), fileName).getAbsolutePath();
    }
}
