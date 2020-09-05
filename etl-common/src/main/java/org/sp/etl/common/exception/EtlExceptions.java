package org.sp.etl.common.exception;

public class EtlExceptions {

    public static abstract class EtlAppException extends Exception {
        public EtlAppException(String message) {
            super(message);
        }

        public EtlAppException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class ObjectNotFoundException extends EtlAppException {
        public ObjectNotFoundException(String message) {
            super(message);
        }

        public ObjectNotFoundException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class InvalidConfigurationException extends EtlAppException {
        public InvalidConfigurationException(String message) {
            super(message);
        }

        public InvalidConfigurationException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class SystemFailureException extends EtlAppException {
        public SystemFailureException(String message) {
            super(message);
        }

        public SystemFailureException(String message, Throwable cause) {
            super(message, cause);
        }
    }


}
