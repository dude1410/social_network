package JavaPRO.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RegisterConfirmRequest {

    @JsonProperty
    private Integer userId;
    @JsonProperty
    private String token;


}
