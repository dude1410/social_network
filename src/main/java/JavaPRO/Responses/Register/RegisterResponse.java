package JavaPRO.Responses.Register;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RegisterResponse {

    @JsonProperty
    private String error;
    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long timestamp;
    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private RegisterResponseData data;
    @JsonProperty("error_Description")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String errorDescription;


    //OK response
    public RegisterResponse(String error, Long timestamp, RegisterResponseData data) {
        this.error = error;
        this.timestamp = timestamp;
        this.data = data;
    }

    //Bad response
    public RegisterResponse(String error, String errorDescription) {
        this.error = error;
        this.errorDescription = errorDescription;
    }

    public String getError() {
        return error;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public RegisterResponseData getData() {
        return data;
    }

    public String getErrorDescription() {
        return errorDescription;
    }
}
