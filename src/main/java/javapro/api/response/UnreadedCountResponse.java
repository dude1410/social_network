package javapro.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UnreadedCountResponse {

    private String error;
    private Long timestamp;
    private UnreadedCountData data;
}
