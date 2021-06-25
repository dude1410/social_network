package JavaPRO.api.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@Data
@NoArgsConstructor
public class LikeRequest {
    @Column(name = "item_id")
    Integer item_id;

    String type;
}
