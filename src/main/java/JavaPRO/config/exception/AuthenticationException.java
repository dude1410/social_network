package JavaPRO.config.exception;

public class AuthenticationException extends AppException {

    private String error;

    private String errorDescription;

    public AuthenticationException(String errorDescription) {
        super("AUTHENTICATION_FAILED", errorDescription);
    }
}
