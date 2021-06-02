package JavaPRO.api.response;

import JavaPRO.model.DTO.Auth.AuthorizedPerson;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@AllArgsConstructor
public class LoginResponce implements Response{


    private String error;

    private Long timestamp;

    private AuthorizedPerson data;
}
