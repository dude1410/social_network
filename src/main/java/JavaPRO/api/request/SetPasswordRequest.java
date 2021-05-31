package JavaPRO.api.request;

import lombok.Data;

@Data
public class SetPasswordRequest {

    String token;

    String password;
}
