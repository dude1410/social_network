package javapro.api.request;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommentBodyRequest {

    @JsonProperty(value = "parent_id")
    @Schema(name = "parent_id", description = "id родительского комментария, если есть", example = "12")
    private Integer parentID;

    @JsonProperty(value = "comment_text")
    @Schema(name = "comment_text", description = "текст комментария", example = "какой-то текст html")
    private String commentText;
}
