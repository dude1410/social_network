package JavaPRO.api.response;

import JavaPRO.model.DTO.Auth.AuthorizedPerson;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponce implements Response{


    private String error;

    private Long timestamp;

    private AuthorizedPerson data;
}
