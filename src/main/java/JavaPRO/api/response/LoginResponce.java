package JavaPRO.api.response;

import JavaPRO.model.DTO.Auth.AuthorizedPerson;
<<<<<<< HEAD
import lombok.AllArgsConstructor;
import lombok.Data;
=======
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
>>>>>>> 1c10703 (login second iteration 29.05.2021)

@Data
@AllArgsConstructor
public class LoginResponce implements Response{


    private String error;

    private Long timestamp;
<<<<<<< HEAD

=======
    @JsonProperty
>>>>>>> 1c10703 (login second iteration 29.05.2021)
    private AuthorizedPerson data;
}
