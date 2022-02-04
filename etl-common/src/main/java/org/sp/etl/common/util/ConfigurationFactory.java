package org.sp.etl.common.util;

import org.sp.etl.common.exception.EtlExceptions;
import org.sp.etl.common.model.Configuration;
import org.sp.etl.common.model.ConfigurationType;
import org.sp.etl.common.model.JsonConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class ConfigurationFactory {

    public static Configuration parse(File configFilePath, ConfigurationType configurationType) throws EtlExceptions.InvalidConfigurationException {


        if (configurationType == ConfigurationType.JSON) {
            InputStream configStream = null;
            try {
                configStream = FileIO.loadFile(configFilePath);
                return new JsonConfiguration(DataUtils.getObjectMapper().readTree(configStream));
            } catch (IOException e) {
                throw new EtlExceptions.InvalidConfigurationException("error occurred while parsing json config", e);
            } finally {
                if (configStream != null) {
                    try {
                        configStream.close();
                    } catch (IOException e) {
                        throw new EtlExceptions.InvalidConfigurationException("error occurred while closing config file", e);
                    }
                }
            }
        } else {
            throw new EtlExceptions.InvalidConfigurationException("parsing not supported for config type - " + configurationType.name());
        }

    }

}
