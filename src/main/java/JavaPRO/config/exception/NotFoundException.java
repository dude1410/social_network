package JavaPRO.config.exception;

public class NotFoundException extends AppException {

    private String error;

    private String errorDescription;

    public NotFoundException(String errorDescription) {
        super("NOT_FOUND", errorDescription);
    }
}
