package JavaPRO.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResponseData {

    @JsonProperty
    private String message;

    public ResponseData(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
