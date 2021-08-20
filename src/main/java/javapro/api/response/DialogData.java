package javapro.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import javapro.model.dto.DialogMessageDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DialogData implements Comparable<DialogData>{

    private Integer id;
    @JsonProperty(value = "unread_count")
    private Integer unreadCount;
    @JsonProperty(value = "last_message")
    private DialogMessageDTO lastMessage;

    @Override
    public int compareTo(@NonNull DialogData o) {
        return this.getLastMessage().getTime().compareTo(o.getLastMessage().getTime());
    }
}
