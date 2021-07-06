package javapro.config.exception;

public class NotFoundException extends AppException {

    public NotFoundException(String errorDescription) {
        super("NOT_FOUND", errorDescription);
    }
}
