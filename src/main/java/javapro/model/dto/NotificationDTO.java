package javapro.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class NotificationDTO {
    private int id;
    @JsonProperty(value = "entity_author")
    private EntityAuthorDTO entityAuthor;
    @JsonProperty(value = "event_type")
    private String eventType;
    private String info;
    @JsonProperty(value = "sent_time")
    private Long sentTime;

}
