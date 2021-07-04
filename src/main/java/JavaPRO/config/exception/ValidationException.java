package JavaPRO.config.exception;

public class ValidationException extends AppException{

    public ValidationException(String errorDescription) {
        super("VALIDATION_ERROR", errorDescription);
    }
}
