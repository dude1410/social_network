package javapro.api.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
public class OnlyMailRequest {

    @NotEmpty
    @Schema(description = "почта пользователя для восстановления пароля", example = "sndl@mail.ru")
    private String email;

}
