package org.sp.etl.common.repo;

import org.sp.etl.common.model.ConfigurationType;
import org.sp.etl.common.util.EtlConstants;

import java.util.Map;

public class RepositoryParameter {

    private ConfigurationType configurationType;
    private Map<String, String> parameters;

    public RepositoryParameter(Map<String, String> parameters) {
        this.parameters = parameters;
        this.configurationType = ConfigurationType.getConfigurationType(parameters.get(EtlConstants.CONFIGURATION_TYPE_FIELD));
    }

    public ConfigurationType getConfigurationType() {
        return configurationType;
    }

    public String getParameter(String key) {
        return this.parameters.get(key);
    }

    public String getParameter(String key, String defaultValue) {
        return this.parameters.getOrDefault(key, defaultValue);
    }

}
