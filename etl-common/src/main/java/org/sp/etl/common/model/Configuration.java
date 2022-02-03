package org.sp.etl.common.model;

import java.util.Iterator;
import java.util.Map;

public interface Configuration {
    void importFrom(Configuration dataObject);

    String getStringValue(String fieldName);

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
}