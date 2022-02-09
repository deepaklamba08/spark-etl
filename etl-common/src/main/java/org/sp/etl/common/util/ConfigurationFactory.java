package org.sp.etl.common.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import org.sp.etl.common.exception.EtlExceptions;
import org.sp.etl.common.model.Configuration;
import org.sp.etl.common.model.ConfigurationType;
import org.sp.etl.common.model.JsonConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

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

    public static Configuration fromMap(Map<String, Object> contents, ConfigurationType configurationType) throws EtlExceptions.InvalidConfigurationException {
        if (configurationType == ConfigurationType.JSON) {
            JsonNode node = DataUtils.getObjectMapper().convertValue(contents, new TypeReference<JsonNode>() {
            });
            return new JsonConfiguration(node);
        } else {
            throw new EtlExceptions.InvalidConfigurationException("parsing not supported for config type - " + configurationType.name());
        }

    }

}

