package javapro.model.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class NotificationTypeDTO {

    private boolean enable;
    private String type;
}
