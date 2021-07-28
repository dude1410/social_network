package javapro.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import javapro.model.dto.DialogMessageDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DialogData {

    private Integer id;
    private Integer unread_count;
    @JsonProperty(value = "last_message")
    private DialogMessageDTO lastMessage;
}
