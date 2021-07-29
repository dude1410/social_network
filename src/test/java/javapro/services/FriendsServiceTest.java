package javapro.services;

import javapro.api.request.IsFriendRequest;
import javapro.api.response.FriendsResponse;
import javapro.api.response.IsFriendResponse;
import javapro.api.response.IsOneFriendResponse;
import javapro.api.response.OkResponse;
import javapro.config.exception.AuthenticationException;
import javapro.config.exception.BadRequestException;
import javapro.config.exception.NotFoundException;
import javapro.model.Friendship;
import javapro.model.Person;
import javapro.model.dto.PersonDTO;
import javapro.model.enums.FriendshipStatus;
import javapro.repository.FriendshipRepository;
import javapro.repository.PersonRepository;
import javapro.util.PersonToPersonDTOMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class FriendsServiceTest {

    // Test objects
    @InjectMocks
    FriendsService friendsService;

    @Mock
    PersonRepository personRepository;

    @Mock
    FriendshipRepository friendshipRepository;

    @Mock
    Authentication auth;

    @Mock
    PersonToPersonDTOMapper mapper;

    @BeforeEach
    void initSecurityContext() {
        when(auth.getName()).thenReturn("dev");
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    void getFriendsWithNameFullList() throws AuthenticationException, NotFoundException {

        // prepare test data
        List<Person> personList = new ArrayList<>();
        personList.add(new Person());

        // subs
        when(friendsService.checkPersonByEmail()).thenReturn(1);
        when(personRepository.findAllFriendsByName(eq("nick"), eq(1))).thenReturn(personList);
        when(mapper.convertToDto(any())).thenReturn(new PersonDTO());

        // run test
        ResponseEntity<FriendsResponse> response = friendsService.getFriends("nick", 0L, 20L);

        // check
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(response.getStatusCodeValue(), 200);
        assertEquals("successfully", response.getBody().getError());
        assertEquals(1L, response.getBody().getTotal());
        assertEquals(0L, response.getBody().getOffset());
        assertEquals(20L, response.getBody().getPerPage());
        assertEquals(1, response.getBody().getData().size());
    }

    @Test
    void getFriendsNoNameFullList() throws AuthenticationException, NotFoundException {

        // prepare test data
        List<Person> personList = new ArrayList<>();
        personList.add(new Person());
        personList.add(new Person());

        // subs
        when(friendsService.checkPersonByEmail()).thenReturn(1);
        when(personRepository.findAllFriends(eq(1))).thenReturn(personList);
        when(mapper.convertToDto(any())).thenReturn(new PersonDTO());

        // run test
        ResponseEntity<FriendsResponse> response = friendsService.getFriends(null, 0L, 20L);

        // check
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(response.getStatusCodeValue(), 200);
        assertEquals("successfully", response.getBody().getError());
        assertEquals(2L, response.getBody().getTotal());
        assertEquals(0L, response.getBody().getOffset());
        assertEquals(20L, response.getBody().getPerPage());
        assertEquals(2, response.getBody().getData().size());
    }

    @Test
    void getFriendsWithNameEmptyList() throws AuthenticationException, NotFoundException {

        // prepare test data
        List<Person> personList = new ArrayList<>();

        // subs
        when(friendsService.checkPersonByEmail()).thenReturn(1);
        when(personRepository.findAllFriendsByName(eq("nick"), eq(1))).thenReturn(personList);

        // run test
        Exception exception = assertThrows(NotFoundException.class, () -> {
            ResponseEntity<FriendsResponse> response = friendsService.getFriends("nick", 0L, 20L);
        });
        String expectedMessage = "Друзей по запросу не найдено";
        String actualMessage = exception.getMessage();

        // check
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void getFriendsNoNameEmptyList() throws AuthenticationException, NotFoundException {

        // prepare test data
        List<Person> personList = new ArrayList<>();

        // subs
        when(friendsService.checkPersonByEmail()).thenReturn(1);
        when(personRepository.findAllFriends(eq(1))).thenReturn(personList);

        // run test
        Exception exception = assertThrows(NotFoundException.class, () -> {
            ResponseEntity<FriendsResponse> response = friendsService.getFriends(null, 0L, 20L);
        });
        String expectedMessage = "Друзей по запросу не найдено";
        String actualMessage = exception.getMessage();

        // check
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void deleteFriendSuccess() throws AuthenticationException, NotFoundException, BadRequestException {

        // prepare test data
        Person person = new Person();

        // subs
        when(friendsService.checkPersonByEmail()).thenReturn(1);
        when(personRepository.findPersonById(any())).thenReturn(person);
        when(friendshipRepository.findFriendshipByUsers(eq(1), any())).thenReturn(new Friendship());

        // run test
        ResponseEntity<OkResponse> response = friendsService.deleteFriend(2);

        // check
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(response.getStatusCodeValue(), 200);
        assertEquals("successfully", response.getBody().getError());
        assertEquals("ok", response.getBody().getData().getMessage());
        verify(friendshipRepository, Mockito.times(1)).delete(any());
    }

    @Test
    void deleteFriendFailNoFriendshipFound() {

        // prepare test data

        // subs
        when(personRepository.findUserIdByEmail(eq("dev"))).thenReturn(1);
        when(personRepository.findPersonById(2)).thenReturn(new Person());
        when(friendshipRepository.findFriendshipByUsers(eq(1), eq(2))).thenReturn(null);

        // run test
        Exception exception = assertThrows(BadRequestException.class, () -> {
            ResponseEntity<OkResponse> response = friendsService.deleteFriend(2);
        });
        String expectedMessage = "Друзей по запросу не найдено";
        String actualMessage = exception.getMessage();

        // check
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void deleteFriendFail_FailCheckPersonById_NullId() {

        // prepare test data

        // subs
        when(personRepository.findUserIdByEmail(eq("dev"))).thenReturn(1);

        // run test
        Exception exception = assertThrows(BadRequestException.class, () -> {
            ResponseEntity<OkResponse> response = friendsService.deleteFriend(null);
        });
        String expectedMessage = "Не передан id пользователя";
        String actualMessage = exception.getMessage();

        // check
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void deleteFriendFail_FailCheckPersonById_NullPerson() {

        // prepare test data

        // subs
        when(personRepository.findUserIdByEmail(eq("dev"))).thenReturn(1);
        when(personRepository.findPersonById(eq(0))).thenReturn(null);

        // run test
        Exception exception = assertThrows(NotFoundException.class, () -> {
            ResponseEntity<OkResponse> response = friendsService.deleteFriend(0);
        });
        String expectedMessage = "Пользователь не найден.";
        String actualMessage = exception.getMessage();

        // check
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void addFriendSuccess() throws AuthenticationException, NotFoundException, BadRequestException {

        // prepare test data

        // subs
        when(personRepository.findUserIdByEmail(eq("dev"))).thenReturn(1);
        when(personRepository.findPersonById(eq(2))).thenReturn(new Person());
        when(friendshipRepository.findFriendshipRequest(eq(1), eq(2))).thenReturn(new Friendship());

        // run test
        ResponseEntity<OkResponse> response = friendsService.addFriend(2);

        // check
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("successfully", response.getBody().getError());
        assertEquals("ok", response.getBody().getData().getMessage());
    }

    @Test
    void addFriendFail_NoRequestInDB() throws AuthenticationException, NotFoundException, BadRequestException {

        // prepare test data

        // subs
        when(personRepository.findUserIdByEmail(eq("dev"))).thenReturn(1);
        when(personRepository.findPersonById(eq(2))).thenReturn(new Person());
        when(friendshipRepository.findFriendshipRequest(eq(1), eq(2))).thenReturn(null);

        // run test
        Exception exception = assertThrows(BadRequestException.class, () -> {
            ResponseEntity<OkResponse> response = friendsService.addFriend(2);
        });

        // check
        String expectedMessage = "Заявка на добавление в друзья не найдена";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void getRequestListNoNameSuccess() throws AuthenticationException, NotFoundException {

        // prepare test data
        List<Person> personList = new ArrayList<>();
        personList.add(new Person());
        personList.add(new Person());

        // subs
        when(personRepository.findUserIdByEmail(eq("dev"))).thenReturn(1);
        when(personRepository.findAllRequestsById(eq(1))).thenReturn(personList);
        when(mapper.convertToDto(any())).thenReturn(new PersonDTO());

        // run test
        ResponseEntity<FriendsResponse> response = friendsService.getRequestList(null, 0L, 20L);

        // check
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(response.getStatusCodeValue(), 200);
        assertEquals("successfully", response.getBody().getError());
        assertEquals(2L, response.getBody().getTotal());
        assertEquals(0L, response.getBody().getOffset());
        assertEquals(20L, response.getBody().getPerPage());
        assertEquals(2, response.getBody().getData().size());
    }

    @Test
    void getRequestListWithNameSuccess() throws AuthenticationException, NotFoundException {

        // prepare test data
        List<Person> personList = new ArrayList<>();
        personList.add(new Person());
        personList.add(new Person());

        // subs
        when(personRepository.findUserIdByEmail(eq("dev"))).thenReturn(1);
        when(personRepository.findAllRequestsByIdAndName(eq(1), eq("nick"))).thenReturn(personList);
        when(mapper.convertToDto(any())).thenReturn(new PersonDTO());

        // run test
        ResponseEntity<FriendsResponse> response = friendsService.getRequestList("nick", 0L, 20L);

        // check
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(response.getStatusCodeValue(), 200);
        assertEquals("successfully", response.getBody().getError());
        assertEquals(2L, response.getBody().getTotal());
        assertEquals(0L, response.getBody().getOffset());
        assertEquals(20L, response.getBody().getPerPage());
        assertEquals(2, response.getBody().getData().size());
    }

    @Test
    void getRequestListNoNameFail_EmptyList() {

        // prepare test data

        // subs
        when(personRepository.findUserIdByEmail(eq("dev"))).thenReturn(1);
        when(personRepository.findAllRequestsById(eq(1))).thenReturn(new ArrayList<>());

        // run test
        Exception exception = assertThrows(NotFoundException.class, () -> {
            ResponseEntity<FriendsResponse> response = friendsService.getRequestList(null, 0L, 20L);
        });

        // check
        String expectedMessage = "Заявка на добавление в друзья не найдена";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void getRequestListWithNameFail_EmptyList() {

        // prepare test data

        // subs
        when(personRepository.findUserIdByEmail(eq("dev"))).thenReturn(1);
        when(personRepository.findAllRequestsByIdAndName(eq(1), eq("nick"))).thenReturn(new ArrayList<>());

        // run test
        Exception exception = assertThrows(NotFoundException.class, () -> {
            ResponseEntity<FriendsResponse> response = friendsService.getRequestList(null, 0L, 20L);
        });

        // check
        String expectedMessage = "Заявка на добавление в друзья не найдена";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void checkFriendStatusSuccess() throws AuthenticationException, NotFoundException, BadRequestException {

        // prepare test data
        List<Integer> userIds = new ArrayList<>();
        userIds.add(1);
        userIds.add(2);
        userIds.add(3);
        IsFriendRequest request = new IsFriendRequest(userIds);
        request.setUserIds(userIds);

        Person main = new Person();
        main.setId(100);

        Friendship friendship1 = new Friendship();
        friendship1.setId(1);
        friendship1.setStatus(FriendshipStatus.FRIEND);
        friendship1.setDstPersonId(main);
        Person person1 = new Person();
        person1.setId(1);
        friendship1.setSrcPersonId(person1);
        Friendship friendship2 = new Friendship();
        friendship2.setId(2);
        friendship2.setStatus(FriendshipStatus.REQUEST);
        friendship2.setDstPersonId(main);
        Person person2 = new Person();
        person2.setId(2);
        friendship2.setSrcPersonId(person2);
        Friendship friendship3 = new Friendship();
        friendship3.setId(3);
        friendship3.setStatus(FriendshipStatus.BLOCKED);
        friendship3.setDstPersonId(main);
        Person person3 = new Person();
        person3.setId(3);
        friendship3.setSrcPersonId(person3);

        // subs
        when(personRepository.findUserIdByEmail(eq("dev"))).thenReturn(100);
        when(personRepository.findPersonById(any())).thenReturn(new Person());
        when(friendshipRepository.findFriendshipByUsers(eq(100), eq(1))).thenReturn(friendship1);
        when(friendshipRepository.findFriendshipByUsers(eq(100), eq(2))).thenReturn(friendship2);
        when(friendshipRepository.findFriendshipByUsers(eq(100), eq(3))).thenReturn(friendship3);

        // run test
        ResponseEntity<IsFriendResponse> response = friendsService.checkFriendStatus(request);

        // check
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(3, response.getBody().getData().size());
        assertEquals("FRIEND", response.getBody().getData().get(0).getStatus());
        assertEquals("REQUEST", response.getBody().getData().get(1).getStatus());
        assertEquals("BLOCKED", response.getBody().getData().get(2).getStatus());
    }

    // todo: продолжить с проверки на пустой список айдишников при входе
    // todo: потом еще одна проверка для метода - когда между юзерами нет статуса
}
