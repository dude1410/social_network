package JavaPRO.model.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CommentDTO {

    @JsonProperty(value = "parent_id")
    private Long parentComment;

    @JsonProperty(value = "comment_text")
    private String commentText;

    @JsonProperty(value = "id")
    private Long id;

    @JsonProperty(value = "post_id")
    private Long postID;

    @JsonProperty(value = "time")
    private Long time;

    @JsonProperty(value = "author_id")
    private Long authorID;

    @JsonProperty(value = "is_blocked")
    private boolean isBlocked;

}
