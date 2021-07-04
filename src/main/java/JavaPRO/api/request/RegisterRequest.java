package JavaPRO.api.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
public class RegisterRequest {

    @NotEmpty
    @Schema(description = "почта пользователя для регистрации", example = "sndl@mail.ru")
    private String email;

    @NotEmpty
    @Schema(description = "Пароль, указанный для регистрации", example = "Ss123456")
    private String passwd1;

    @NotEmpty
    @Schema(description = "Пароль, указанный для регистрации повторно", example = "Ss123456")
    private String passwd2;

    @NotEmpty
    @Schema(description = "Имя нового пользователя", example = "Иван")
    private String firstName;

    @NotEmpty
    @Schema(description = "фамилия нового пользователя", example = "Иванов")
    private String lastName;

    @NotEmpty
    @Schema(description = "4-ех значный цифровой код с картинки", example = "1234")
    private String code;
}
