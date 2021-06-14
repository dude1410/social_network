package JavaPRO.api.response;

import JavaPRO.model.DTO.PostCommentDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostCommentResponse implements Response  {

    private String error;

    private Long timestamp;

    private Long total;

    private Long offset;

    private Long perPage;

    private PostCommentDTO data;
}
