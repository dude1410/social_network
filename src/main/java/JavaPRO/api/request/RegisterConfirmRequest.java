package JavaPRO.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RegisterConfirmRequest {

    @JsonProperty
    Integer userId;
    @JsonProperty
    String token;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
