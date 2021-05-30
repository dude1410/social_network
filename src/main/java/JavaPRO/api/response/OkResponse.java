package JavaPRO.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;


public class OkResponse {

    @JsonProperty
    private String error;
    @JsonProperty
    private Long timestamp;
    @JsonProperty
    private ResponseData data;


    public OkResponse(String error, Long timestamp, ResponseData data) {
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

    public ResponseData getData() {
        return data;
    }

    public void setData(ResponseData data) {
        this.data = data;
    }
}
