package javapro.services;

import javapro.api.request.IsFriendRequest;
import javapro.api.response.*;
import javapro.config.Config;
import javapro.config.exception.AuthenticationException;
import javapro.config.exception.BadRequestException;
import javapro.config.exception.NotFoundException;
import javapro.model.Friendship;
import javapro.model.Notification;
import javapro.model.NotificationEntity;
import javapro.model.Person;
import javapro.model.dto.PersonDTO;
import javapro.model.enums.FriendshipStatus;
import javapro.model.enums.NotificationType;
import javapro.repository.FriendshipRepository;
import javapro.repository.NotificationEntityRepository;
import javapro.repository.NotificationRepository;
import javapro.repository.PersonRepository;
import javapro.util.PersonToPersonDTOMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class FriendsService {

    private final PersonRepository personRepository;
    private final PersonToPersonDTOMapper personToPersonDTOMapper;
    private final FriendshipRepository friendshipRepository;
    private final NotificationEntityRepository notificationEntityRepository;
    private final NotificationRepository notificationRepository;

    @Autowired
    public FriendsService(PersonRepository personRepository,
                          PersonToPersonDTOMapper personToPersonDTOMapper,
                          FriendshipRepository friendshipRepository,
                          NotificationEntityRepository notificationEntityRepository,
                          NotificationRepository notificationRepository) {
        this.personRepository = personRepository;
        this.personToPersonDTOMapper = personToPersonDTOMapper;
        this.friendshipRepository = friendshipRepository;
        this.notificationEntityRepository = notificationEntityRepository;
        this.notificationRepository = notificationRepository;
    }

    // получение всех друзей пользователя

    public ResponseEntity<FriendsResponse> getFriends(String name,
                                                      Long offset,
                                                      Long itemPerPage) throws AuthenticationException,
            NotFoundException {

        int userId = checkPersonByEmail().getId();
        List<Person> allFriends;
        if (name == null) {
            allFriends = personRepository.findAllFriends(userId);
        } else {
            allFriends = personRepository.findAllFriendsByName(name, userId);
        }

        if (allFriends.isEmpty()) {
            List<PersonDTO> personDTOList = new ArrayList<>();
            return ResponseEntity.ok(new FriendsResponse(Config.WALL_RESPONSE,
                    new Timestamp(System.currentTimeMillis()).getTime(),
                    0L,
                    offset,
                    itemPerPage,
                    personDTOList));
        }

        List<PersonDTO> personDTOList = personToPersonDTO(allFriends);

        return ResponseEntity.ok(new FriendsResponse(Config.WALL_RESPONSE,
                new Timestamp(System.currentTimeMillis()).getTime(),
                (long) personDTOList.size(),
                offset,
                itemPerPage,
                personDTOList));
    }

    // удаление из друзей

    public ResponseEntity<OkResponse> deleteFriend(Integer id) throws AuthenticationException,
            NotFoundException,
            BadRequestException {

        Integer userId = checkPersonByEmail().getId();
        checkPersonById(id);

        Friendship friendship = friendshipRepository.findFriendshipByUsers(userId, id);
        if (friendship == null) {
            throw new BadRequestException(Config.STRING_NO_FRIENDS_FOUND);
        }

        friendshipRepository.delete(friendship);

        return ResponseEntity.ok(new OkResponse(Config.WALL_RESPONSE,
                new Timestamp(System.currentTimeMillis()).getTime(),
                new ResponseData("ok")));
    }



    public ResponseEntity<OkResponse> addFriend(Integer id) throws AuthenticationException,
            NotFoundException,
            BadRequestException {

        Integer userId = checkPersonByEmail().getId();
        checkPersonById(id);

        Friendship friendshipInDB = friendshipRepository.findFriendshipRequest(userId, id);
        if (friendshipInDB == null) {
            throw new BadRequestException(Config.STRING_NO_FRIENDSHIP_REQUEST);
        }
        friendshipInDB.setStatus(FriendshipStatus.FRIEND);
        friendshipRepository.save(friendshipInDB);


        return ResponseEntity.ok(new OkResponse(Config.WALL_RESPONSE,
                new Timestamp(System.currentTimeMillis()).getTime(),
                new ResponseData("ok")));
    }


    private Person checkPersonByEmail() throws AuthenticationException, NotFoundException {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        if (userEmail == null) {
            throw new AuthenticationException(Config.STRING_AUTH_ERROR);
        }
        var person = personRepository.findUserIdByEmail(userEmail);
        if (person == null) {
            throw new NotFoundException(Config.STRING_AUTH_LOGIN_NO_SUCH_USER);
        }
        return person;
    }

    private void checkPersonById(Integer id) throws BadRequestException,
            NotFoundException {
        if (id == null) {
            throw new BadRequestException(Config.STRING_NO_USER_ID);
        }
        Person personInDB = personRepository.findPersonById(id);
        if (personInDB == null) {
            throw new NotFoundException(Config.STRING_AUTH_LOGIN_NO_SUCH_USER);
        }
    }

    public ResponseEntity<FriendsResponse> getRequestList(String name,
                                                          Long offset,
                                                          Long itemPerPage)
            throws AuthenticationException, NotFoundException {

        Integer userId = checkPersonByEmail().getId();
        if (offset == null) {
            offset = 0L;
        }
        if (itemPerPage == null) {
            itemPerPage = 20L;
        }
        List<Person> allRequests;
        if (name == null) {
            allRequests = personRepository.findAllRequestsById(userId);
        } else {
            allRequests = personRepository.findAllRequestsByIdAndName(userId, name);
        }
        if (allRequests.isEmpty()) {
            List<PersonDTO> personDTOS = new ArrayList<>();
            ResponseEntity.ok(new FriendsResponse(Config.WALL_RESPONSE,
                    new Timestamp(System.currentTimeMillis()).getTime(),
                    0L,
                    offset,
                    itemPerPage,
                    personDTOS));
        }

        List<PersonDTO> personDTOS = personToPersonDTO(allRequests);

        return ResponseEntity.ok(new FriendsResponse(Config.WALL_RESPONSE,
                new Timestamp(System.currentTimeMillis()).getTime(),
                (long) personDTOS.size(),
                offset,
                itemPerPage,
                personDTOS));
    }

    private ArrayList<PersonDTO> personToPersonDTO(List<Person> personList) {
        ArrayList<PersonDTO> personDTOList = new ArrayList<>();
        for (Person person : personList) {
            var convertedPerson = personToPersonDTOMapper.convertToDto(person);
            personDTOList.add(convertedPerson);
        }
        return personDTOList;
    }

    public ResponseEntity<IsFriendResponse> checkFriendStatus(IsFriendRequest request)
            throws AuthenticationException, NotFoundException, BadRequestException {

        Integer userId = checkPersonByEmail().getId();

        List<Integer> idsToCheck = request.getUserIds();

        if (idsToCheck == null) {
            throw new NotFoundException(Config.STRING_NO_FRIENDS_FOUND);
        }

        List<IsOneFriendResponse> response = new ArrayList<>();

        for (Integer id : idsToCheck) {
            checkPersonById(id);
            Friendship friendship = friendshipRepository.findFriendshipByUsers(userId, id);

            IsOneFriendResponse newResponse = new IsOneFriendResponse();
            newResponse.setUserId(id);

            if (friendship == null) {
                newResponse.setStatus("NO_STATUS");
            } else {
                newResponse.setStatus(friendship.getStatus().toString());
            }

            response.add(newResponse);
        }

        return ResponseEntity.ok(new IsFriendResponse(response));
    }
//    рекомендации
    public ResponseEntity<FriendsResponse> getRecommendations(Long offset, Long itemPerPage)
            throws AuthenticationException, NotFoundException {

        Integer userId = checkPersonByEmail().getId();

        if (offset == null) {
            offset = 0L;
        }
        if (itemPerPage == null) {
            itemPerPage = 20L;
        }

        List<Person> allRecommendations = personRepository.findRecommendations(userId);
        if (allRecommendations.isEmpty()) {
            List<PersonDTO> personDTOS = new ArrayList<>();

            return ResponseEntity.ok(new FriendsResponse(Config.WALL_RESPONSE,
                    new Timestamp(System.currentTimeMillis()).getTime(),
                    0L,
                    offset,
                    itemPerPage,
                    personDTOS));
        }
        ArrayList<PersonDTO> personDTOS = personToPersonDTO(allRecommendations);
        var currentFriends = personRepository.findAllFriends(userId);

        ArrayList<PersonDTO> returnPersonList = new ArrayList<>();
        for (PersonDTO personDTO : personDTOS) {
            var n = 0;
            for (Person element : currentFriends) {
                if (personDTO.getId() == (long) element.getId()) {
                    n++;
                }
            }
            if (n == 0) {
                returnPersonList.add(personDTO);
            }
        }

        return ResponseEntity.ok(new FriendsResponse(Config.WALL_RESPONSE,
                new Timestamp(System.currentTimeMillis()).getTime(),
                (long) personDTOS.size(),
                offset,
                itemPerPage,
                returnPersonList));
    }

    public ResponseEntity<OkResponse> sendRequest(Integer id)
            throws BadRequestException, NotFoundException, AuthenticationException {

        Integer userId = checkPersonByEmail().getId();
        checkPersonById(id);

        var authorN = personRepository.findByEmail(SecurityContextHolder
                .getContext()
                .getAuthentication().getName());
        var targetPerson = personRepository.findById(id).orElseThrow(() -> new NotFoundException(Config.STRING_AUTH_LOGIN_NO_SUCH_USER));

        if (userId.equals(id)) {
            throw new BadRequestException(Config.STRING_BAD_REQUEST);
        }

        Friendship friendship = friendshipRepository.findFriendshipByUsers(userId, id);
        if (friendship == null) {
            Friendship newFriendship = new Friendship();
            newFriendship.setStatus(FriendshipStatus.REQUEST);
            newFriendship.setTime(new Timestamp(System.currentTimeMillis()));
            newFriendship.setSrcPersonId(personRepository.findPersonById(userId));
            newFriendship.setDstPersonId(personRepository.findPersonById(id));
            friendshipRepository.save(newFriendship);
        } else {
            switch (friendship.getStatus()) {
                case REQUEST:
                    throw new BadRequestException(Config.STRING_REQUEST_REPEATED);
                case FRIEND:
                    throw new BadRequestException(Config.STRING_USER_IS_ALREADY_YOUR_FRIEND);
                case BLOCKED:
                case DECLINED:
                case SUBSCRIBED:
                    throw new BadRequestException(Config.STRING_BAD_REQUEST);
            }
        }
        Runnable task = () -> {
            try {
                var notificationEntity = new NotificationEntity();
                notificationEntity.setPerson(authorN);
                var notificationEnt = notificationEntityRepository.save(notificationEntity);
                var notification = new Notification();
                notification.setSentTime(new Timestamp(System.currentTimeMillis()));
                notification.setEntity(notificationEnt);
                notification.setNotificationType(NotificationType.FRIEND_REQUEST);
                notification.setPerson(targetPerson);
                notification.setInfo("Не имей сто рублей, а имей сто друзей.");
                notificationRepository.save(notification);
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        var thread = new Thread(task);
        thread.start();

        return ResponseEntity.ok(new OkResponse(Config.WALL_RESPONSE,
                new Timestamp(System.currentTimeMillis()).getTime(),
                new ResponseData("ok")));
    }
}
