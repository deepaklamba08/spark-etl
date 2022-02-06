package org.sp.etl.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang.text.StrBuilder;

public class DataUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public static ObjectNode createObjectNode() {
        return objectMapper.createObjectNode();
    }

    public static String makeString(String separator, String... fields) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < fields.length; i++) {
            result.append(fields[i]);
            if (i < fields.length - 1) {
                result.append(separator);
            }
        }
        return result.toString();
    }

    public static String makeString(String prefix, String separator, String... fields) {
        return new StringBuilder(prefix)
                .append(DataUtils.makeString(separator, fields))
                .toString();
    }

    public static String makeString(String prefix, String suffix, String separator, String... fields) {
        return new StringBuilder(prefix)
                .append(DataUtils.makeString(separator, fields))
                .append(suffix)
                .toString();
    }

}
