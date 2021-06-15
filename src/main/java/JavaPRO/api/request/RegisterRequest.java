package JavaPRO.api.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RegisterRequest {

    @Schema(description = "почта пользователя для регистрации", example = "sndl@mail.ru")
    private String email;

    @Schema(description = "Пароль, указанный для регистрации", example = "Ss123456")
    private String passwd1;

    @Schema(description = "Пароль, указанный для регистрации повторно", example = "Ss123456")
    private String passwd2;

    @Schema(description = "Имя нового пользователя", example = "Иван")
    private String firstName;

    @Schema(description = "фамилия нового пользователя", example = "Иванов")
    private String lastName;

    @Schema(description = "4-ех значный цифровой код с картинки", example = "1234")
    private String code;
}
