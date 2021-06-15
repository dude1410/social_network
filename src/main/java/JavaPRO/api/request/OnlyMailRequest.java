package JavaPRO.api.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OnlyMailRequest {

    @Schema(description = "почта пользователя для восстановления пароля", example = "sndl@mail.ru")
    private String email;

}
