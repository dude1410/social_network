package JavaPRO.api.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginRequest {

    @Schema(description = "почтовый ящик", example = "mymail@mail.com")
    private String email;

    @Schema(description = "пароль", example = "Pass123456")
    private String password;



}


