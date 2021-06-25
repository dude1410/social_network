package JavaPRO.api.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@Data
@NoArgsConstructor
public class PostUpdateRequest {

    @Schema(description = "новое название поста", example = "new post title")
    private String title;

    @Schema(description = "новый текст поста", example = "Здесь у нас новый текст поста")
    @Column(name = "post_text")
    private String post_text;
}
