package JavaPRO.config.exception;

public abstract class AppException extends Exception {

    private String error;

    private String errorDescription;

    protected AppException (String error, String errorDescription){
        super(errorDescription);
        this.error = error;
        this.errorDescription = errorDescription;
    }

    public String getError() {
        return error;
    }

    public String getErrorDescription() {
        return errorDescription;
    }
}
