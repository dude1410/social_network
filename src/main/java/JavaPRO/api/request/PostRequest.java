package JavaPRO.api.request;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PostRequest {

    private String text;

    private Integer date_from;

    private Integer date_to;

    private Long offset;

    private Long itemPerPage;
}
