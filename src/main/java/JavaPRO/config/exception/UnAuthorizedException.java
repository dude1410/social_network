package JavaPRO.config.exception;

public class UnAuthorizedException extends AppException {
        private String error;

        private String errorDescription;

        public UnAuthorizedException(String errorDescription) {
            super("UNAUTHORIZED", errorDescription);
        }

}
