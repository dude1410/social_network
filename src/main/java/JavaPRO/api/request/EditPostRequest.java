package JavaPRO.api.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EditPostRequest {

    @Schema(description = "новое название поста", example = "new post title")
    private String title;

    @Schema(description = "новый текст поста", example = "new post text html")
    private String postText;
}
