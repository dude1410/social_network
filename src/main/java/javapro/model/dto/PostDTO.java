package javapro.model.dto;

import javapro.model.dto.auth.AuthorizedPerson;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class PostDTO {

    @JsonProperty(value = "id")
    private Integer id;

    @JsonProperty(value = "time")
    private Long time;

    @JsonProperty(value = "author")
    private AuthorizedPerson author;

    @JsonProperty(value = "title")
    private String title;

    @JsonProperty(value = "post_text")
    private String postText;

    @JsonProperty(value = "is_blocked")
    private Boolean isBlocked;

    @JsonProperty(value = "likes")
    private Integer likes ;

    @JsonProperty(value = "comments")
    private List<CommentDTO> postComments;

    @JsonProperty(value = "type")
    private String postStatus;

    @JsonProperty(value = "tags")
    private List<TagDTO> tags;

}
