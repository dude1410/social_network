package JavaPRO.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;


public class RegisterErrorResponse {

    @JsonProperty
    String error;
    @JsonProperty("error_description")
    String errorDescription;

    public RegisterErrorResponse(String error, String errorDescription) {
        this.error = error;
        this.errorDescription = errorDescription;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }
}
