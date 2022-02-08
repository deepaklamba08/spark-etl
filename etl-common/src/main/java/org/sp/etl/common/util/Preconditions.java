package org.sp.etl.common.util;

import org.sp.etl.common.model.Configuration;

public class Preconditions {

    public static void validateFields(Configuration configuration, String message, String... fields) {
        boolean isValid = true;

        for (int i = 0; i < fields.length && isValid; i++) {
            isValid = isValid && configuration.hasField(fields[i]);
        }
        if (!isValid) {
            throw new IllegalStateException(message);
        }

    }

    public static void validateFields(Configuration configuration, String... fields) {
        StringBuilder message = new StringBuilder("configuration has missing fields - ");
        int missingCount = 0;
        for (int i = 0; i < fields.length; i++) {
            if (!configuration.hasField(fields[i])) {
                message.append(fields[i]);
                missingCount++;
            }
        }
        if (missingCount > 0) {
            throw new IllegalStateException(message.toString());
        }

    }

    public static void checkNull(Object object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }
}
