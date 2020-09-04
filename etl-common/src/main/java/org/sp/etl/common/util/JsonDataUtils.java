package org.sp.etl.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class JsonDataUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public static ObjectNode createObjectNode() {
        return objectMapper.createObjectNode();
    }
}
