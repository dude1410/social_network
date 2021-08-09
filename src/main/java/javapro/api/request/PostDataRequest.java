package javapro.api.request;

import io.swagger.v3.oas.annotations.media.Schema;
import javapro.model.dto.TagDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class PostDataRequest {

    @Schema(name = "title", description = "новое название поста", example = "new post title")
    private String title;

    @Schema(name = "post_text", description = "новый текст поста", example = "Здесь у нас новый текст поста")
    private String post_text;

    @Schema(name = "tags", description = "список тэгов", example = "Новые тэги")
    private List<TagRequest> tags;
}
