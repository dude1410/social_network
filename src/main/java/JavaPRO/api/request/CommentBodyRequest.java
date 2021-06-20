package JavaPRO.api.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommentBodyRequest {

    private Integer parent_id;

    private String comment_text;
}
