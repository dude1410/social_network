package JavaPRO.api.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MyWallRequest {

    private Integer id;

    private Long offset;

    private Long itemPerPage;
}
