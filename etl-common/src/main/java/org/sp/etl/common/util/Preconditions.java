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
}
