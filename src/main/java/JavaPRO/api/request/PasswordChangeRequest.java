package JavaPRO.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
public class PasswordChangeRequest {

    @NotEmpty
    @Schema(description = "новый пароль пользователя при смене", example = "!qaz@Wsx")
    @JsonProperty("password")
    private String newPassword;
}
