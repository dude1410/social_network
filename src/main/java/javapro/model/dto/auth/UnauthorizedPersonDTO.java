package javapro.model.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class UnauthorizedPersonDTO {



    @Schema(description = "e-mail пользователя", example = "my@email.com")
    @JsonProperty(value = "email")
    private String email;

    @Schema(description = "пароль пользователя", example = "dHdf6dDHfd")
    private String password;

}
