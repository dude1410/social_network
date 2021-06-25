package JavaPRO.api.response;

import JavaPRO.model.DTO.IsLikedDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class IsLikedResponse {

    @Schema(description = "ошибка", example = "invalid password")
    private String error;

    @Schema(description = "время", example = "2147483648L")
    private Long timestamp;

    private IsLikedDTO data;
}
