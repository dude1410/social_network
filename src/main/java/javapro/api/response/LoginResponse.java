package javapro.api.response;

import javapro.model.dto.auth.AuthorizedPerson;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {

    @Schema(description = "ошибка", example = "invalid password")
    private String error;

    @Schema(description = "время", example = "2147483648L")
    private Long timestamp;

    @Schema(description = "объект AuthorizedPerson")
    private AuthorizedPerson data;
}
