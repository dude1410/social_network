package JavaPRO.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;


public class RegisterResponse {

    @JsonProperty
    private String error;
    @JsonProperty
    private Long timestamp;
    @JsonProperty
    private RegisterResponseData data;


    public RegisterResponse(String error, Long timestamp, RegisterResponseData data) {
        this.error = error;
        this.timestamp = timestamp;
        this.data = data;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public RegisterResponseData getData() {
        return data;
    }

    public void setData(RegisterResponseData data) {
        this.data = data;
    }
}
