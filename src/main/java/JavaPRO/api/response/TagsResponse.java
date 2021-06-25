package JavaPRO.api.response;

import JavaPRO.model.DTO.TagDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class TagsResponse {
    @Schema(description = "ошибка", example = "success")
    private String error;

    @Schema(description = "метка времени", example = "2147483648L")
    private Long timestamp;

    private Integer total;

    private Integer offset;

    private Integer perPage;

    private List<TagDTO> data;

}
