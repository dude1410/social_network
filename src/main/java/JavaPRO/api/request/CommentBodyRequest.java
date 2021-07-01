package JavaPRO.api.request;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@Data
@NoArgsConstructor
public class CommentBodyRequest {

    @Schema(name = "parent_id", description = "id родительского комментария, если есть", example = "12")
    private Integer parent_id;

    @Schema(name = "comment_text", description = "текст комментария", example = "какой-то текст html")
    private String comment_text;
}
