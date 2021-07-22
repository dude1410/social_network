package javapro.services;

import javapro.api.request.NotificationSetupRequest;
import javapro.api.response.PlatformResponse;
import javapro.api.response.Response;
import javapro.config.Config;
import javapro.config.exception.AuthenticationException;
import javapro.config.exception.NotFoundException;
import javapro.model.NotificationSetup;
import javapro.model.Person;
import javapro.model.dto.MessageDTO;
import javapro.model.dto.NotificationTypeDTO;
import javapro.model.enums.NotificationType;
import javapro.repository.NotificationSetupRepository;
import javapro.repository.PersonRepository;
import javapro.util.Time;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationService {

    private final PersonRepository personRepository;
    private final NotificationSetupRepository notificationSetupRepository;

    public NotificationService(PersonRepository personRepository,
                               NotificationSetupRepository notificationSetupRepository) {
        this.personRepository = personRepository;
        this.notificationSetupRepository = notificationSetupRepository;
    }

    public ResponseEntity<PlatformResponse> getNotification(Long offset, Long itemPerPage) throws AuthenticationException, NotFoundException {
/*
        заготовка под будущее:
        с проверкой авторизации
        int personId;
        var person = isAuthorize();
        if (person == null) {
            throw new NotFoundException(Config.STRING_AUTH_LOGIN_NO_SUCH_USER);
        } else {
            personId = person.getId();
        }
*/


        offset = (offset == null) ? 0 : offset;
        itemPerPage = (itemPerPage == null) ? 20 : itemPerPage;

        var response = new PlatformResponse<>();

        response.setError("ok");
        response.setTimestamp(Time.getTime());
        response.setTotal(0);
        response.setOffset(Math.toIntExact(offset));
        response.setPerPage(Math.toIntExact(itemPerPage));
        response.setData(new ArrayList());
        return ResponseEntity.ok(response);
    }


    public ResponseEntity<Response> getAccountNotification() throws AuthenticationException, NotFoundException {

        int personId;
        var person = isAuthorize();
        if (person == null) {
            throw new NotFoundException(Config.STRING_AUTH_LOGIN_NO_SUCH_USER);
        } else {
            personId = person.getId();
        }

        var notificationSetupList = getNotificationSetup(personId);

        if (notificationSetupList.size() == 0) {
            var saveNotificationList = new ArrayList<NotificationSetup>();
            for (NotificationType element : NotificationType.values()) {
                var notification = new NotificationSetup();
                notification.setPersonId(personId);
                notification.setNotificationtype(element.name());
                notification.setEnable(true);
                saveNotificationList.add(notification);
            }
            notificationSetupRepository.saveAll(saveNotificationList);
        }

        var response = new Response<>();
        response.setError("ok");
        response.setTimestamp(Time.getTime());

        var notificationDataFromBd = notificationSetupRepository.findAllByPersonId(personId);

        if (notificationDataFromBd.size() == 0) {
            throw new NotFoundException(Config.STRING_NOT_FOUND_NOTIFICATION_SETUP);
        }

        var notificationData = new ArrayList<NotificationTypeDTO>();

        for (NotificationSetup element : notificationDataFromBd) {
            var notificationTypeDTO = new NotificationTypeDTO();
            notificationTypeDTO.setType(element.getNotificationtype());
            notificationTypeDTO.setEnable(element.getEnable());
            notificationData.add(notificationTypeDTO);

        }
        response.setData(notificationData);
        return ResponseEntity.ok(response);
    }


    public ResponseEntity<Response> setAccountNotification(NotificationSetupRequest request) throws AuthenticationException, NotFoundException {

        int personId;
        var person = isAuthorize();
        if (person == null) {
            throw new NotFoundException(Config.STRING_AUTH_LOGIN_NO_SUCH_USER);
        } else {
            personId = person.getId();
        }
        var response = new Response<MessageDTO>();
        String notificationType = request.getNotificationType();
        Boolean enable = request.getEnable();

        for (NotificationType element : NotificationType.values()) {
            if (notificationType.equals(element.toString())) {
                var notification = notificationSetupRepository
                        .findByNotificationtypeAndPersonId(request.getNotificationType(), personId);
                if (notification == null) {
                    throw new NotFoundException(Config.STRING_NOT_FOUND_NOTIFICATION_SETUP);
                }
                notification.setEnable(enable);
                notificationSetupRepository.save(notification);
                response.setError("параметр изменен");
                response.setTimestamp(Time.getTime());
                var message = new MessageDTO();
                message.setMessage("параметр изменен");
                response.setData(message);
            }

        }
        return ResponseEntity.ok(response);
    }


    private Person isAuthorize() throws AuthenticationException {

        if (!SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
            throw new AuthenticationException(Config.STRING_AUTH_ERROR);
        }

        return personRepository.findByEmail(SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName());
    }

    private List<NotificationSetup> getNotificationSetup(Integer personId) {
        return notificationSetupRepository.findAllByPersonId(personId);
    }


}
