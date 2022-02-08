package org.sp.etl.common.util;

import org.sp.etl.common.exception.EtlExceptions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class FileIO {

    public static InputStream loadFile(File filePath) throws EtlExceptions.InvalidConfigurationException {
        try {
            return new FileInputStream(filePath);
        } catch (FileNotFoundException e) {
            throw new EtlExceptions.InvalidConfigurationException("could not find data file - " + filePath.getPath());
        }
    }
}
