package JavaPRO.model.DTO.Auth;

import JavaPRO.config.Config;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@ToString
public class UnauthorizedPersonDTO {

    @JsonProperty(value = "e_mail")
    @NotBlank(message = Config.STRING_FIELD_CANT_BE_BLANK)
    @Email(message = Config.STRING_AUTH_INVALID_EMAIL)
    @Schema(description = "e-mail пользователя", example = "my@email.com")
    private final String email;

    @NotBlank(message = Config.STRING_FIELD_CANT_BE_BLANK)
    @Size(min = 6, max = 255, message = Config.STRING_AUTH_SHORT_PASSWORD)
    @Schema(description = "пароль пользователя", example = "dHdf6dDHfd")
    private final String password;


//TODO: слить с LoginRequest
}
