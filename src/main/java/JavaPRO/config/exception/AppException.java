package JavaPRO.config.exception;

public class AppException extends Exception {

    private final String error;

    protected AppException (String error, String errorDescription){
        super(errorDescription);
        this.error = error;
    }

    public String getError() {
        return error;
    }
}
