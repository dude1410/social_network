package JavaPRO.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RegisterResponseData {

    @JsonProperty
    private String message;

    public RegisterResponseData(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
