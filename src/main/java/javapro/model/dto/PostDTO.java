package javapro.model.dto;

import javapro.model.enums.PostStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class PostDTO {

    @JsonProperty(value = "id")
    private Integer id;

    @JsonProperty(value = "time")
    private Long time;

    @JsonProperty(value = "author")
    private PersonDTO author;

    @JsonProperty(value = "title")
    private String title;

    @JsonProperty(value = "post_text")
    private String postText;

    @JsonProperty(value = "is_blocked")
    private Boolean isBlocked;

    @JsonProperty(value = "likes")
    private Integer likes ;

    @JsonProperty(value = "comments")
    private List<CommentViewDTO> postComment;

    @JsonProperty(value = "type")
    private String postStatus;

    public void setPostStatus() {
        postStatus = (time > new Timestamp(System.currentTimeMillis()).getTime() ? PostStatus.QUEUED : PostStatus.POSTED).toString();
    }

}
