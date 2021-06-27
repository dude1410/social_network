package JavaPRO.api.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EditCommentRequest {

    private Integer parent_id;

    private String comment_text;
}
