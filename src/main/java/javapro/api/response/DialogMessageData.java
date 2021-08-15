package javapro.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import javapro.model.enums.ReadStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DialogMessageData {

    private Integer id;
    @JsonProperty(value = "author_id")
    private Integer authorId;
    @JsonProperty(value = "recipient")
    private RecipientData recipientData;
    @JsonProperty(value = "message_text")
    private String messageText;
    @JsonProperty(value = "read_status")
    private ReadStatus readStatus;
    private Boolean isSentByMe;
}
