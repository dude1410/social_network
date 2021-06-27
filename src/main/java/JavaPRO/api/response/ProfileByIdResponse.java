package JavaPRO.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileByIdResponse {

    private String error;
    private Long timestamp;
    private ProfileByIdData data;
}
