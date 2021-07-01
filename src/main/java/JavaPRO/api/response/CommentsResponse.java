package JavaPRO.api.response;

import JavaPRO.model.DTO.CommentDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CommentsResponse {

    @Schema(description = "ошибка", example = "success")
    private String error;

    @Schema(description = "метка времени", example = "2147483648L")
    private Long timestamp;

    @Schema(description = "общее количество комментов", example = "45l")
    private int total;

    @Schema(description = "сдвиг для постраничного вывода", example = "0l")
    private int offset;

    @Schema(description = "количество комментов на страницу", example = "20l")
    private int perPage;

    @Schema(description = "объект CommentDTO")
    List<CommentDTO> data;

}
