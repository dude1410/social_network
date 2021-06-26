package JavaPRO.api.request;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@Data
@NoArgsConstructor
public class CommentBodyRequest {

    @Column(name = "parent_id")
    private Integer parent_id;

    @Column(name = "comment_text")
    private String comment_text;
}
