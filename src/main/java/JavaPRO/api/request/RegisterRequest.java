package JavaPRO.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegisterRequest {

    @JsonProperty
    private String email;
    @JsonProperty
    private String passwd1;
    @JsonProperty
    private String passwd2;
    @JsonProperty
    private String firstName;
    @JsonProperty
    private String lastName;
    @JsonProperty
    private String code;
}
