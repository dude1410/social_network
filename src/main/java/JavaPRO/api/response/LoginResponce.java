package JavaPRO.api.response;

import JavaPRO.model.DTO.Auth.AuthorizedPerson;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
public class LoginResponce {

    @JsonProperty
    private String error;
    @JsonProperty
    private Long timestamp;
    @JsonProperty
    private AuthorizedPerson data;
}
