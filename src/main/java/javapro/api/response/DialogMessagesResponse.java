package javapro.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DialogMessagesResponse {

    private String error;
    private Long timestamp;
    private Integer total;
    private Integer offset;
    private Integer perPage;
    private List<DialogMessageData> data;
}
