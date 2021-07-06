package javapro.api.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PostDataRequest {

    @Schema(name = "title", description = "новое название поста", example = "new post title")
    private String title;

    @Schema(name = "post_text", description = "новый текст поста", example = "Здесь у нас новый текст поста")
    private String post_text;
}
