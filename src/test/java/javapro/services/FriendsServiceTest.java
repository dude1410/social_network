package javapro.services;

import javapro.api.response.FriendsResponse;
import javapro.api.response.OkResponse;
import javapro.config.exception.AuthenticationException;
import javapro.config.exception.BadRequestException;
import javapro.config.exception.NotFoundException;
import javapro.model.Friendship;
import javapro.model.Person;
import javapro.model.dto.PersonDTO;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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
    void deleteFriendFailUnsuccess_CheckPersonById_NullId() {

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
    void deleteFriendFailUnsuccess_CheckPersonById_NullPerson() {

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

    // todo продолжить с метода addFriend: friendship не найден в БД




}
