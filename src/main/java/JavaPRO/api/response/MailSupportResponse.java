package JavaPRO.api.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MailSupportResponse {

    @Schema(description = "метка отправки обращения", example = "true")
    private boolean isSent;

    @Schema(description = "текст для возврата на фронт", example = "Ваше обращение в службу поддержки направлено")
    private String text;
}
