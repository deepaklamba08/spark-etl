package org.sp.etl.common.model;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public interface Configuration extends Identifiable {
    void importFrom(Configuration dataObject);

    boolean hasField(String fieldName);

    String getStringValue(String fieldName);

    String getStringValue(String fieldName, String defaultValue);

    boolean getBooleanValue(String fieldName);

    boolean getBooleanValue(String fieldName, boolean defaultValue);

    List<String> getListValue(String fieldName);

    List<String> getListValue(String fieldName, List<String> defaultValue);

    int getIntValue(String fieldName);

    Configuration getAttribute(String fieldName);

    Iterator<String> getFields();

    Map<String, String> getValueMap(String fieldName);

    void setAttribute(String fieldName, Configuration attribute);

    void setAttribute(String fieldName, String attribute);

    void setAttribute(String fieldName, long attribute);

    boolean isArray();

    boolean isArray(String fieldName);

    boolean isObject();

    boolean isObject(String fieldName);

    boolean isNull();

    List<Configuration> getAsList();

    Map<String, String> getAsMap();

    <T> Map<String, T> getAsMap(Function<Configuration, T> mapper);

    public Configuration getConfiguration(String fieldName);

    public Configuration getConfiguration(String fieldName, Configuration defaultValue);
}