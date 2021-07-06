package javapro.api.response;

import javapro.model.dto.LikeDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LikeResponse {
    @Schema(description = "ошибка", example = "invalid password")
    private String error;

    @Schema(description = "время", example = "2147483648L")
    private Long timestamp;

    @Schema(description = "объект LikeDTO")
    private LikeDTO data;
}
