package org.sp.etl.common.model;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

public interface Configuration extends Identifiable {
    void importFrom(Configuration dataObject);

    boolean hasField(String fieldName);

    String getStringValue(String fieldName);

    Date getDateValue(String fieldName);

    Date getDateValue(String fieldName, Date defaultValue);

    String getStringValue(String fieldName, String defaultValue);

    boolean getBooleanValue(String fieldName);

    boolean getBooleanValue(String fieldName, boolean defaultValue);

    List<String> getListValue(String fieldName);

    List<String> getListValue(String fieldName, List<String> defaultValue);

    int getIntValue(String fieldName);

    long getLongValue(String fieldName);

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

    public void merge(Configuration other);

    //public void remove(Configuration other);

    public void toArray();

    List<Configuration> getAsList();

    Map<String, String> getAsMap();

    <T> Map<String, T> getAsMap(Function<Configuration, T> mapper);

    public Configuration getConfiguration(String fieldName);

    public Configuration getConfiguration(String fieldName, Configuration defaultValue);
}