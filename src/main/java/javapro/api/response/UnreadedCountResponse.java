package javapro.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UnreadedCountResponse {

    private String error;
    private Long timestamp;
    private UnreadedCountData data;
}
