package JavaPRO.api.request;

import JavaPRO.config.Config;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
public class MailSupportRequest {
    
    @NotEmpty
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")
    @Schema(description = "почтовый ящик отправителя запроса для обратной связи", example = "mymail@mail.com")
    private String email;

    @NotEmpty
    @Size(min = 20, message = Config.STRING_MAIL_TO_SUPPORT_SHORT_TEXT)
    @Schema(description = "текст обращения", example = "прошу вас помочь в решении моего вопроса")
    private String text;
}
