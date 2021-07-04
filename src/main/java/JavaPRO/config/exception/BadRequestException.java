package JavaPRO.config.exception;

public class BadRequestException extends AppException {

    public BadRequestException(String errorDescription) {
        super("INVALID_REQUEST", errorDescription);
    }
}
