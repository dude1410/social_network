package JavaPRO.api.request;

import JavaPRO.config.Config;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
public class MailSupportRequest {

    @Schema(description = "почтовый ящик отправителя запроса для обратной связи", example = "mymail@mail.com")
    private String email;

    @Size(min = 20, message = Config.STRING_MAIL_TO_SUPPORT_SHORT_TEXT)
    @Schema(description = "текст обращения", example = "прошу вас помочь в решении моего вопроса")
    private String text;
}
