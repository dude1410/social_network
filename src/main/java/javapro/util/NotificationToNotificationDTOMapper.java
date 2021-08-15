package javapro.util;

import javapro.model.Notification;
import javapro.model.dto.EntityAuthorDTO;
import javapro.model.dto.NotificationDTO;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;


@Component
public class NotificationToNotificationDTOMapper {


    private final ModelMapper modelMapper;

    public NotificationToNotificationDTOMapper() {
        this.modelMapper = new ModelMapper();

        modelMapper.createTypeMap(Notification.class, NotificationDTO.class);
        modelMapper.createTypeMap(javapro.model.NotificationEntity.class, EntityAuthorDTO.class);
    }

    public NotificationDTO convertToDto(Notification entity) {
        return modelMapper.map(entity, NotificationDTO.class);
    }
}











