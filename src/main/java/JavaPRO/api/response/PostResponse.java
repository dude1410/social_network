package JavaPRO.api.response;

import JavaPRO.model.DTO.PostDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PostResponse implements Response {

    private String error;

    private Long timestamp;

    private Long total;

    private Long offset;

    private Long itemPerPage;

    private List<PostDTO> data;

}
