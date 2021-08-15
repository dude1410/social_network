package javapro.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class PostDataRequest {

    @JsonProperty(value = "title")
    @Schema(name = "title", description = "новое название поста", example = "new post title")
    private String title;

    @JsonProperty(value = "post_text")
    @Schema(name = "post_text", description = "новый текст поста", example = "Здесь у нас новый текст поста")
    private String postText;

    @JsonProperty(value = "tags")
    @Schema(name = "tags", description = "список тэгов")
    private List<TagRequest> tags;
}
