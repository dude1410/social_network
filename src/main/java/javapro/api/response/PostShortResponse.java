package javapro.api.response;

import javapro.model.dto.PostDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostShortResponse {

    @Schema(description = "ошибка", example = "success")
    private String error;

    @Schema(description = "метка времени", example = "2147483648L")
    private Long timestamp;

    @Schema(description = "объект PostDTO")
    private PostDTO post;
}
