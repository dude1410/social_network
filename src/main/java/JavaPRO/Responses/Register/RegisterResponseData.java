package JavaPRO.Responses.Register;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RegisterResponseData {

    @JsonProperty
    private String message;

    public RegisterResponseData(String message) {
        this.message = message;
    }
}
