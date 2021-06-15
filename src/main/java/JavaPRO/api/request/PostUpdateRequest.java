package JavaPRO.api.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PostUpdateRequest {

    private String title;

    private String post_text;
}
