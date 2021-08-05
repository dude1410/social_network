package javapro.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import javapro.model.enums.ReadStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddDialogMessageData {

    private Integer id;
    private Long time;
    @JsonProperty(value = "author_id")
    private Integer authorId;
    @JsonProperty(value = "recipient_id")
    private Integer recipientId;
    @JsonProperty(value = "message_text")
    private String messageText;
    @JsonProperty(value = "read_status")
    private ReadStatus readStatus;
}
