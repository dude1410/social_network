package javapro.config.exception;

public class AuthenticationException extends AppException {

    public AuthenticationException(String errorDescription) {
        super("AUTHENTICATION_FAILED", errorDescription);
    }
}
