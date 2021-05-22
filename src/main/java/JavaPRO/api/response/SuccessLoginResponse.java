package JavaPRO.api.response;

import JavaPRO.model.DTO.UserLoginResponse;
import org.springframework.stereotype.Component;

@Component
public class SuccessLoginResponse {

    private String error;

    private Long timestamp;

    private UserLoginResponse data;

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

    public UserLoginResponse getData() {
        return data;
    }

    public void setData(UserLoginResponse data) {
        this.data = data;
    }
}
