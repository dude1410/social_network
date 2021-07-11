package javapro.api.response;

import javapro.model.dto.PostDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PostResponse {

    @Schema(description = "ошибка", example = "success")
    private String error;

    @Schema(description = "метка времени", example = "2147483648L")
    private Long timestamp;

    @Schema(description = "общее количество постов", example = "45l")
    private Integer total;

    @Schema(description = "сдвиг для постраничного вывода", example = "0l")
    private Integer offset;

    @Schema(description = "количество постов на страницу", example = "20l")
    private Integer itemPerPage;

    @Schema(description = "список объектов PostDTO")
    private List<PostDTO> data;

}
