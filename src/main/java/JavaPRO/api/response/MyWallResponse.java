package JavaPRO.api.response;

import JavaPRO.model.DTO.PostDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class MyWallResponse {

    @Schema(description = "ошибка", example = "invalid_request")
    private String error;

    @Schema(description = "метка времени", example = "2147483648L")
    private Long timestamp;

    @Schema(description = "общее количество постов", example = "45l")
    private Integer total;

    @Schema(description = "сдвиг для постраничного вывода", example = "0l")
    private Integer offset;

    @Schema(description = "количество постов на страницу", example = "20l")
    private Integer perPage;

    @Schema(description = "модель для вывода поста")
    private List<PostDTO> data;
}
