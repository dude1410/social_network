package JavaPRO.api.response;

import JavaPRO.model.DTO.Auth.AuthorizedUser;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponce {

    @JsonProperty
    private String error;
    @JsonProperty
    private Long timestamp;
    @JsonProperty
    private AuthorizedUser data;
}
