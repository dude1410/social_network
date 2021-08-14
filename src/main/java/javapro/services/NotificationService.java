package javapro.services;

import javapro.api.request.NotificationSetupRequest;
import javapro.api.response.PlatformResponse;
import javapro.api.response.Response;
import javapro.config.Config;
import javapro.config.exception.AuthenticationException;
import javapro.config.exception.NotFoundException;
import javapro.model.*;
import javapro.model.dto.EntityAuthorDTO;
import javapro.model.dto.MessageDTO;
import javapro.model.dto.NotificationDTO;
import javapro.model.dto.NotificationTypeDTO;
import javapro.model.enums.FriendshipStatus;
import javapro.model.enums.NotificationType;
import javapro.repository.*;
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

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@EnableScheduling
@Service
public class NotificationService {

    private final PersonRepository personRepository;
    private final NotificationSetupRepository notificationSetupRepository;
    private final NotificationRepository notificationRepository;
    private final FriendshipRepository friendshipRepository;
    private final NotificationEntityRepository notificationEntityRepository;
    private final DeletedPersonRepository deletedPersonRepository;
    private final String TIMEZONE = "Europe/Moscow";
    private final String DATEFORMAT = "MM-dd";

    public NotificationService(PersonRepository personRepository,
                               NotificationSetupRepository notificationSetupRepository,
                               NotificationRepository notificationRepository,
                               FriendshipRepository friendshipRepository,
                               NotificationEntityRepository notificationEntityRepository,
                               DeletedPersonRepository deletedPersonRepository) {
        this.personRepository = personRepository;
        this.notificationSetupRepository = notificationSetupRepository;
        this.notificationRepository = notificationRepository;
        this.friendshipRepository = friendshipRepository;
        this.notificationEntityRepository = notificationEntityRepository;
        this.deletedPersonRepository = deletedPersonRepository;
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


    public ResponseEntity<Response<Object>> getAccountNotificationSetup() throws AuthenticationException, NotFoundException {

        int personId;
        var person = isAuthorize();
        if (person == null) {
            throw new NotFoundException(Config.STRING_AUTH_LOGIN_NO_SUCH_USER);
        } else {
            personId = person.getId();
        }

        var notificationSetupList = getNotificationSetup(personId);

        if (notificationSetupList.isEmpty()) {
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

        if (notificationDataFromBd.isEmpty()) {
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


    public ResponseEntity<PlatformResponse<Object>> readNotifications(Integer id) throws NotFoundException {
        int personId = personRepository.findByEmailForLogin(SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName())
                .getId();
        var notification = notificationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Config.STRING_NOTIFICATION_ISDELETED));
        notificationRepository.deleteById(notification.getId());
        return ResponseEntity.ok(createResponse(personId));
    }


    public ResponseEntity<PlatformResponse<Object>> readAllNotifications() {
        int personId = personRepository.findByEmailForLogin(SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName())
                .getId();
        var notification = notificationRepository.findAllByPersonId(personId);
        ArrayList<NotificationEntity> entity = new ArrayList<>();
        notification.forEach(el -> {
            var notificationEntity = notificationEntityRepository.findById(el.getEntity().getId());
            notificationEntity.ifPresent(entity::add);
        });
        notificationRepository.deleteAll(personId);
        return ResponseEntity.ok(createResponse(personId));
    }

    private PlatformResponse<Object> createResponse(Integer personId) {
        var notificationDTOArrayList = getListNotifications(personId);
        var response = new PlatformResponse<>();
        response.setError("ok");
        response.setTimestamp(Time.getTime());
        response.setTotal(notificationDTOArrayList.size());
        response.setOffset(0);
        response.setPerPage(20);
        response.setData(notificationDTOArrayList);
        return response;
    }


    private ArrayList<NotificationDTO> getListNotifications(Integer personId) {

        var notificationSetup = notificationSetupRepository.findAllByPersonId(personId);
        HashMap<String, Boolean> setupData = new HashMap<>();
        notificationSetup.forEach(element -> setupData.put(element.getNotificationtype(), element.getEnable()));
        Pageable pageable = PageRequest.of(0, 10, Sort.by("sentTime").descending());
        Page<Notification> entity = notificationRepository.findAllByPersonId(pageable, personId);
        ArrayList<NotificationDTO> notificationDTOArrayList = new ArrayList<>();
        entity.forEach(el -> {
            if (!el.getEntity().getPerson().getId().equals(personId)) {
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
                    notificationDTO.setInfo(el.getInfo());
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


    @Scheduled(cron = "0 0 1 * * ?") // 1 раз в день в 1-00 ночи по мск
//    @Scheduled(cron = "0 */1 * ? * *") //каждую 1 минуту
    private void addFriendsBirthdayNotification() {
        notificationRepository.deleteAllByNotificationType(NotificationType.FRIEND_BIRTHDAY);
        var friendshipList = friendshipRepository.findAllByStatus(FriendshipStatus.FRIEND);
        var today = LocalDate.now(ZoneId.of(TIMEZONE)).format(DateTimeFormatter.ofPattern(DATEFORMAT));
        for (Friendship element : friendshipList) {

            if (element.getDstPersonId().getBirthDate() != null) {
                var dstPersonBirthDay = element.getDstPersonId().getBirthDate().toInstant().atZone(ZoneId.of(TIMEZONE))
                        .toLocalDate().format(DateTimeFormatter.ofPattern(DATEFORMAT));
                if (dstPersonBirthDay.equals(today) && !element.getDstPersonId().isBlocked()) {
                    addNotification(element.getDstPersonId().getId(), element.getSrcPersonId().getId());
                }


            }
            if (element.getSrcPersonId().getBirthDate() != null) {
                var srcPersonBirthDay = element.getSrcPersonId().getBirthDate().toInstant().atZone(ZoneId.of(TIMEZONE))
                        .toLocalDate().format(DateTimeFormatter.ofPattern(DATEFORMAT));

                if (srcPersonBirthDay.equals(today) && !element.getSrcPersonId().isBlocked()) {
                    addNotification(element.getSrcPersonId().getId(), element.getDstPersonId().getId());
                }

            }
        }

    }

    private void addNotification(Integer authorPerson, Integer targetPerson) {
        var notification = new Notification();
        var author = personRepository.findPersonById(authorPerson);
        var target = personRepository.findPersonById(targetPerson);

        if (deletedPersonRepository.findByPersonId(targetPerson).isPresent() ||
                deletedPersonRepository.findByPersonId(authorPerson).isPresent()) {
            return;
        }

        var notificationEntity = new NotificationEntity();
        notificationEntity.setPerson(author);
        var entity = notificationEntityRepository.save(notificationEntity);
        notification.setEntity(entity);
        notification.setPerson(target);
        notification.setInfo("Не забудьте поздравить его");
        notification.setNotificationType(NotificationType.FRIEND_BIRTHDAY);
        notification.setSentTime(new Timestamp(Time.getTime()));

        notificationRepository.save(notification);


    }
}
