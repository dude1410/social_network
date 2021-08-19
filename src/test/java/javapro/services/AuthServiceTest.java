package javapro.services;

import javapro.api.response.LoginResponse;
import javapro.config.exception.BadRequestException;
import javapro.config.exception.NotFoundException;
import javapro.model.Country;
import javapro.model.DeletedPerson;
import javapro.model.Person;
import javapro.model.Town;
import javapro.model.dto.auth.UnauthorizedPersonDTO;
import javapro.repository.DeletedPersonRepository;
import javapro.repository.PersonRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AuthServiceTest {


    //    Test object
    @InjectMocks
    private AuthService authService;

    @Mock
    PersonRepository personRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private DeletedPersonRepository deletedPersonRepository;

    @Mock
    private HttpServletRequest httpServletRequest;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

//    @BeforeEach
//    void initSecurityContext() {
//        when(auth.getName()).thenReturn("dev");
//
//        SecurityContextHolder.getContext().setAuthentication(auth);
//    }

//    @Test
//    @WithMockUser(username = "dev", password = "good")
    public void successLoginUser() throws BadRequestException, NotFoundException {
//        enter data
        var country = new Country();
        country.setId(1);
        country.setName("Чубурстан");

        var town = new Town();
        town.setCountryId(1);
        town.setId(1);
        town.setName("Простоквашино");

        var user = new UnauthorizedPersonDTO();
        user.setEmail("dev");
        user.setPassword("good");

        var person = new Person();
        person.setId(1);
        person.setEmail("dev");
        person.setPassword("good");
//        person.setFirstName("abram");
//        person.setLastName("abramyan");
//        person.setRegDate(new Timestamp(Time.getTime()));
//        person.setApproved(true);
//        person.setBlocked(false);
//        person.setMessagesPermission(MessagesPermission.ALL);
//        person.setLastOnlineTime(new Timestamp(Time.getTime()));
//        person.setRole(1);
//        person.setCountryId(country);
//        person.setTownId(town);
//        person.setBirthDate(new Timestamp(Time.getTime()));
//        person.setPhone("88888888888");
//        person.setPhoto("photo");
//        person.setAbout("jhjhjhjhjhjhjh");
//        person.setConfirmationCode("hghghghghghghgh");


        when(personRepository.findByEmailForLogin(user.getEmail())).thenReturn(person);
        when(deletedPersonRepository.findPerson(person.getId())).thenReturn(null);
        when(passwordEncoder.matches(user.getPassword(), person.getPassword())).thenReturn(true);

        ResponseEntity<LoginResponse> response = authService.loginUser(user, httpServletRequest);

        assertEquals("dev", SecurityContextHolder.getContext().getAuthentication().getName());

    }

    @Test
    public void notApprovedLoginUser() {
        var user = new UnauthorizedPersonDTO();
        user.setEmail("dev");
        user.setPassword("good");

        var person = new Person();
        person.setId(1);
        person.setEmail("dev");
        person.setPassword("good");
        person.setApproved(false);

        when(personRepository.findByEmailForLogin(user.getEmail())).thenReturn(person);
        when(deletedPersonRepository.findPerson(person.getId())).thenReturn(null);
        when(passwordEncoder.matches(user.getPassword(), person.getPassword())).thenReturn(true);

        Exception exception = assertThrows(BadRequestException.class, () -> {
            ResponseEntity<LoginResponse> response = authService.loginUser(user, httpServletRequest);
        });

        String expectedMessage = "Пользователь не активный или заблокирован";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void isBlockedLoginUser()  {
        var user = new UnauthorizedPersonDTO();
        user.setEmail("dev");
        user.setPassword("good");

        var person = new Person();
        person.setId(1);
        person.setEmail("dev");
        person.setPassword("good");
        person.setBlocked(true);

        when(personRepository.findByEmailForLogin(user.getEmail())).thenReturn(person);
        when(deletedPersonRepository.findPerson(person.getId())).thenReturn(null);
        when(passwordEncoder.matches(user.getPassword(), person.getPassword())).thenReturn(true);

        Exception exception = assertThrows(BadRequestException.class, () -> {
            ResponseEntity<LoginResponse> response = authService.loginUser(user, httpServletRequest);
        });

        String expectedMessage = "Пользователь не активный или заблокирован";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void notFoundLoginUser()  {
        var user = new UnauthorizedPersonDTO();
        user.setEmail("dev");
        user.setPassword("good");

        var person = new Person();
        person.setId(1);
        person.setEmail("dev");
        person.setPassword("good");
        person.setBlocked(false);

        when(personRepository.findByEmailForLogin(user.getEmail())).thenReturn(null);

        Exception exception = assertThrows(NotFoundException.class, () -> {
            ResponseEntity<LoginResponse> response = authService.loginUser(user, httpServletRequest);
        });

        String expectedMessage = "В базе данных не найден пользователь";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void isDeletedLoginUser()  {
        var user = new UnauthorizedPersonDTO();
        user.setEmail("dev");
        user.setPassword("good");

        var person = new Person();
        person.setId(1);
        person.setEmail("dev");
        person.setPassword("good");
        person.setBlocked(false);

        when(personRepository.findByEmailForLogin(user.getEmail())).thenReturn(person);
        when(deletedPersonRepository.findPerson(person.getId())).thenReturn(new DeletedPerson());

        Exception exception = assertThrows(BadRequestException.class, () -> {
            ResponseEntity<LoginResponse> response = authService.loginUser(user, httpServletRequest);
        });

        String expectedMessage = "Пользователь удален";
        String actualMessage = exception.getMessage();

        assertEquals(actualMessage, expectedMessage);
    }

    @Test
    public void wrongPasswordLoginUser()  {
        var user = new UnauthorizedPersonDTO();
        user.setEmail("dev");
        user.setPassword("bad");

        var person = new Person();
        person.setId(1);
        person.setEmail("dev");
        person.setPassword("good");
        person.setBlocked(false);

        when(personRepository.findByEmailForLogin(user.getEmail())).thenReturn(person);
        when(deletedPersonRepository.findPerson(person.getId())).thenReturn(null);
        Exception exception = assertThrows(BadRequestException.class, () -> {
            ResponseEntity<LoginResponse> response = authService.loginUser(user, httpServletRequest);
        });

        String expectedMessage = "Пароль указан неверно.";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void emptyPasswordLoginUser()  {
        var user = new UnauthorizedPersonDTO();
        user.setEmail("dev");
        user.setPassword("");

        var person = new Person();
        person.setId(1);
        person.setEmail("dev");
        person.setPassword("good");
        person.setBlocked(false);

        when(personRepository.findByEmailForLogin(user.getEmail())).thenReturn(person);
        when(deletedPersonRepository.findPerson(person.getId())).thenReturn(null);
        Exception exception = assertThrows(BadRequestException.class, () -> {
            ResponseEntity<LoginResponse> response = authService.loginUser(user, httpServletRequest);
        });

        String expectedMessage = "Адрес или пароль не указаны.";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void emptyEmailLoginUser()  {
        var user = new UnauthorizedPersonDTO();
        user.setEmail("");
        user.setPassword("good");

        var person = new Person();
        person.setId(1);
        person.setEmail("dev");
        person.setPassword("good");
        person.setBlocked(false);

        when(personRepository.findByEmailForLogin(user.getEmail())).thenReturn(person);
        when(deletedPersonRepository.findPerson(person.getId())).thenReturn(null);
        Exception exception = assertThrows(BadRequestException.class, () -> {
            ResponseEntity<LoginResponse> response = authService.loginUser(user, httpServletRequest);
        });

        String expectedMessage = "Адрес или пароль не указаны.";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }
}

