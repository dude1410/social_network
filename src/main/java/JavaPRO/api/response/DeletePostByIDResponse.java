package JavaPRO.api.response;

import JavaPRO.model.DTO.PostDeleteDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DeletePostByIDResponse implements Response {

    private String error;

    private Long timestamp;

    private PostDeleteDTO data;
}
