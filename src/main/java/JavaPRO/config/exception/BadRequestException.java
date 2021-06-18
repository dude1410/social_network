package JavaPRO.config.exception;

public class BadRequestException extends AppException {

    private String error;

    private String errorDescription;

    public BadRequestException(String errorDescription) {
        super("INVALID_REQUEST", errorDescription);
    }
}
