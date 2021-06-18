package JavaPRO.api.response;

import JavaPRO.model.DTO.PostCommentDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostCommentResponse {

    @Schema(description = "ошибка", example = "success")
    private String error;

    @Schema(description = "метка времени", example = "2147483648L")
    private Long timestamp;

    @Schema(description = "общее количество постов", example = "45l")
    private Long total;

    @Schema(description = "сдвиг для постраничного вывода", example = "0l")
    private Long offset;

    @Schema(description = "количество постов на страницу", example = "20l")
    private Long perPage;

    @Schema(description = "объект PostCommentDTO")
    private PostCommentDTO data;
}
