package javapro.config.exception;

public class ValidationException extends AppException{

    public ValidationException(String errorDescription) {
        super("VALIDATION_ERROR", errorDescription);
    }
}
