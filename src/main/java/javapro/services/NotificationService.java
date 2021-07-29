package javapro.services;

import javapro.api.request.NotificationSetupRequest;
import javapro.api.response.PlatformResponse;
import javapro.api.response.Response;
import javapro.config.Config;
import javapro.config.exception.AuthenticationException;
import javapro.config.exception.NotFoundException;
import javapro.model.Notification;
import javapro.model.NotificationSetup;
import javapro.model.Person;
import javapro.model.dto.EntityAuthorDTO;
import javapro.model.dto.MessageDTO;
import javapro.model.dto.NotificationDTO;
import javapro.model.dto.NotificationTypeDTO;
import javapro.model.enums.NotificationType;
import javapro.repository.DeletedPersonRepository;
import javapro.repository.NotificationRepository;
import javapro.repository.NotificationSetupRepository;
import javapro.repository.PersonRepository;
import javapro.util.NotificationToNotificationDTOMaper;
import javapro.util.Time;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@EnableScheduling
@Service
public class NotificationService {

    private final PersonRepository personRepository;
    private final NotificationSetupRepository notificationSetupRepository;
    private final NotificationRepository notificationRepository;


    public NotificationService(PersonRepository personRepository,
                               NotificationSetupRepository notificationSetupRepository,
                               DeletedPersonRepository deletedPersonRepository,
                               NotificationRepository notificationRepository,
                               NotificationToNotificationDTOMaper maper) {
        this.personRepository = personRepository;
        this.notificationSetupRepository = notificationSetupRepository;
        this.notificationRepository = notificationRepository;
    }

    public ResponseEntity<PlatformResponse<Object>> getNotification(Integer offset, Integer itemPerPage) throws AuthenticationException, NotFoundException {


        offset = (offset == null) ? 0 : offset;
        itemPerPage = (itemPerPage == null) ? 20 : itemPerPage;
        int personId;
        var person = isAuthorize();
        if (person == null) {
            throw new NotFoundException(Config.STRING_AUTH_LOGIN_NO_SUCH_USER);
        } else {
            personId = person.getId();
        }

        var notificationDTOArrayList = getListNotifications(personId);
        var response = new PlatformResponse<>();

        response.setError("ok");
        response.setTimestamp(Time.getTime());
        response.setTotal(notificationDTOArrayList.size());
        response.setOffset(offset);
        response.setPerPage(itemPerPage);
        response.setData(notificationDTOArrayList);
        return ResponseEntity.ok(response);
    }


    public ResponseEntity<Response> getAccountNotificationSetup() throws AuthenticationException, NotFoundException {

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


    public ResponseEntity<Response<MessageDTO>> setAccountNotification(NotificationSetupRequest request) throws AuthenticationException, NotFoundException {

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


    public ResponseEntity<PlatformResponse<Object>> readNotifications(Boolean all, Integer id) {
        int personId = personRepository.findByEmailForLogin(SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName())
                .getId();
        if (id != null) {
            notificationRepository.deleteById(id);
        } else {
            notificationRepository.deleteAll(personId);
        }
        var notificationDTOArrayList = getListNotifications(personId);
        var response = new PlatformResponse<>();

        response.setError("ok");
        response.setTimestamp(Time.getTime());
        response.setTotal(notificationDTOArrayList.size());
        response.setOffset(0);
        response.setPerPage(20);
        response.setData(notificationDTOArrayList);
        return ResponseEntity.ok(response);


    }


    private ArrayList<NotificationDTO> getListNotifications(Integer personId) {

        var notificationSetup = notificationSetupRepository.findAllByPersonId(personId);
        HashMap<String, Boolean> setupData = new HashMap<>();
        notificationSetup.forEach(element -> setupData.put(element.getNotificationtype(), element.getEnable()));
        var targetPerson = personRepository.findPersonByApprovedIsTrueAAndBlockedIsFalse(personId);

        Pageable pageable = PageRequest.of(0, 20, Sort.by("sentTime").descending());

        Page<Notification> entity = notificationRepository.findAllByPersonId(pageable, personId);

        ArrayList<NotificationDTO> notificationDTOArrayList = new ArrayList<>();
        entity.forEach(el -> {
            if (el.getEntity().getPerson().getId() != personId) {
                if (setupData.get(el.getNotificationType().toString()).equals(true)) {


                    var notificationDTO = new NotificationDTO();
                    notificationDTO.setId(el.getId());
                    var entityAuthorDTO = new EntityAuthorDTO();
                    entityAuthorDTO.setPhoto(el.getEntity().getPerson().getPhoto());
                    entityAuthorDTO.setFirstName(el.getEntity().getPerson().getFirstName());
                    entityAuthorDTO.setLastName(el.getEntity().getPerson().getLastName());
                    entityAuthorDTO.setId(el.getEntity().getPerson().getId());
                    notificationDTO.setEntityAuthor(entityAuthorDTO);
                    notificationDTO.setEventType(el.getNotificationType().toString());
                    notificationDTO.setSentTime(el.getSentTime().getTime());
                    notificationDTO.setInfo("maperrr");
                    notificationDTOArrayList.add(notificationDTO);
                }
            }
        });
        return notificationDTOArrayList;
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


    @Scheduled(cron = "0 0 12 * * ?")
    private void removeFoulNotifications() {


    }
}
