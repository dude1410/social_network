package javapro.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class NotificationSetupRequest {

    @JsonProperty(value = "notification_type")
    private String notificationType;

    private Boolean enable;

}
