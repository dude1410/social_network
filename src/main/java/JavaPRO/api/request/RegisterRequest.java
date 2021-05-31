package JavaPRO.api.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RegisterRequest {

    private String email;

    private String passwd1;

    private String passwd2;

    private String firstName;

    private String lastName;

    private String code;
}
