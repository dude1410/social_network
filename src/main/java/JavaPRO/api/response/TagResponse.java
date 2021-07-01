package JavaPRO.api.response;

import JavaPRO.model.DTO.TagDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TagResponse {

    @Schema(description = "ошибка", example = "success")
    private String error;

    @Schema(description = "метка времени", example = "2147483648L")
    private Long timestamp;

    @Schema(description = "объект TagDTO")
    private TagDTO data;
}
