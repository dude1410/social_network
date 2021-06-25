package JavaPRO.api.request;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@Data
@NoArgsConstructor
public class PostRequest {

    private String text;

    @Column(name = "date_from")
    private Integer date_from;

    @Column(name = "date_To")
    private Integer date_to;

    private Long offset;

    private Long itemPerPage;
}
