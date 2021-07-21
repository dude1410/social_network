package javapro.services;

import javapro.configs.MainTestConfig;
import javapro.model.Person;
import javapro.model.Token;
import javapro.repository.TokenRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = {TokenRepository.class, TokenService.class})
@Import(MainTestConfig.class)
class TokenServiceTest {

    private final TokenService testableService;

    @MockBean
    private TokenRepository tokenRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Autowired
    public TokenServiceTest(TokenService testableService) {
        this.testableService = testableService;
    }

    @Test
    void testCheckTokenValidToken() {
        String token = "validToken";
        Token tokenReturnedByRepo = new Token();
        tokenReturnedByRepo.setDate(new Date());
        when(tokenRepository.findByToken(token)).thenReturn(tokenReturnedByRepo);
        assertTrue(testableService.checkToken(token));
    }

    @Test
    void testCheckTokenNotValidToken() {
        String token = "notValidToken";
        Token tokenReturnedByRepo = new Token();
        tokenReturnedByRepo.setDate(new Date(System.currentTimeMillis() - 800000));
        when(tokenRepository.findByToken(token)).thenReturn(tokenReturnedByRepo);
        assertFalse(testableService.checkToken(token));
    }

    @Test
    void testCheckTokenNullToken() {
        String token = "emptyToken";
        when(tokenRepository.findByToken(token)).thenReturn(null);
        assertFalse(testableService.checkToken(token));
    }

    @Test
    void testSetNewPersonToken() {
        Person person = new Person();
        person.setEmail("personEmail");
        Token token = new Token();
        String strToken = "token";
        token.setToken(strToken);
        token.setPerson(person);
        verify(passwordEncoder, atMostOnce()).encode(person.getEmail());
        when(tokenRepository.findByPerson(person)).thenReturn(token);
        assertNotEquals(strToken,
                        testableService.setNewPersonToken(person).getToken());
    }

    @Test
    void testSetNewPersonTokenNullToken() {
        Person person = new Person();
        person.setEmail("personEmail");
        verify(passwordEncoder, atMostOnce()).encode(person.getEmail());
        when(tokenRepository.findByPerson(person)).thenReturn(null);
        assertNotNull(testableService.setNewPersonToken(person));
    }

    @Test
    void testFindByToken() {
        Token token = new Token();
        token.setToken("testToken");
        when(tokenRepository.findByToken("testToken")).thenReturn(token);
        assertEquals(token.getToken(),
                     testableService.findToken("testToken").getToken());
    }
}
