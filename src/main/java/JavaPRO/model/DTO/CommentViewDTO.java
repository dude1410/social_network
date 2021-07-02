package JavaPRO.model.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CommentViewDTO {
    @JsonProperty(value = "parent_id")
    private Integer parentComment;

    @JsonProperty(value = "comment_text")
    private String commentText;

    @JsonProperty(value = "id")
    private Integer id;

    @JsonProperty(value = "post_id")
    private Integer postID;

    @JsonProperty(value = "time")
    private Long time;

    @JsonProperty(value = "author")
    private PersonDTO author;

    @JsonProperty(value = "likes")
    private Integer likes;
}
