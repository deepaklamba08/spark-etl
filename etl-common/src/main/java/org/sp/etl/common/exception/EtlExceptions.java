package org.sp.etl.common.exception;

public class EtlExceptions {

    public static class ObjectNotFoundException extends Exception {
        public ObjectNotFoundException(String message) {
            super(message);
        }

        public ObjectNotFoundException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class InvalidConfigurationException extends Exception {
        public InvalidConfigurationException(String message) {
            super(message);
        }

        public InvalidConfigurationException(String message, Throwable cause) {
            super(message, cause);
        }
    }


}
