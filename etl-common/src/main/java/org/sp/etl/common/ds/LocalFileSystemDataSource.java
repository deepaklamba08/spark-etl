package org.sp.etl.common.ds;

import java.io.File;

public class LocalFileSystemDataSource extends FileSystemDataSource {

    @Override
    public String getPath(String fileName) {
        return new File(this.getBaseDirectory(), fileName).getAbsolutePath();
    }
}
