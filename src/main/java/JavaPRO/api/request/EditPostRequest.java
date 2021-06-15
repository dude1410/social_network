package JavaPRO.api.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EditPostRequest {

    private String title;

    private String postText;
}
