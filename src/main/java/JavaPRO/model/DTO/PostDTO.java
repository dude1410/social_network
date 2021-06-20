package JavaPRO.model.DTO;

import JavaPRO.model.ENUM.PostStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class PostDTO {

    @JsonProperty(value = "id")
    private Long id;

    @JsonProperty(value = "time")
    private Long time;

    @JsonProperty(value = "author")
    private PersonDTO author;

    @JsonProperty(value = "title")
    private String title;

    @JsonProperty(value = "post_text")
    private String postText;

    @JsonProperty(value = "is_blocked")
    private Boolean isBlocked = false;

    @JsonProperty(value = "likes")
    private Long likes;

    @JsonProperty(value = "comments")
    private List<CommentDTO> postComment;

    @JsonProperty(value = "type")
    private String postStatus;

    public void setPostStatus() {
        postStatus = (time > new Timestamp(System.currentTimeMillis()).getTime() ? PostStatus.QUEUED : PostStatus.POSTED).toString();
    }
}
