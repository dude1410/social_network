package javapro.services;

import javapro.configs.MainTestConfig;
import javapro.model.Person;
import javapro.model.Token;
import javapro.repository.TokenRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.RepeatedTest;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;
import java.util.HashSet;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.matchesPattern;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {TokenRepository.class, TokenService.class})
@Import(MainTestConfig.class)
class TokenServiceTest {

    private final int TOTAL_ITERATIONS = 500;
    private final int MAX_TOKEN_LENGTH = 50;
    private static HashSet<String> tokenSet;
    private final TokenService testableService;

    @Autowired
    Logger logger;

    @MockBean
    TokenRepository tokenRepository;

    @MockBean
    PasswordEncoder passwordEncoder;

    @Autowired
    public TokenServiceTest(TokenService testableService) {
        this.testableService = testableService;
    }

    @BeforeAll
    static void setUpGetTokenTest() {
        tokenSet = new HashSet<>();
    }

    @RepeatedTest(TOTAL_ITERATIONS)
    void testGetRandomString() {
        try {
            String token = testableService.getToken();
            logger.info("Generated new token: " + token);
            assertNotNull(token);
            logger.info("token not null - OK");
            assertTrue(token.length() > 0 && token.length() <= MAX_TOKEN_LENGTH);
            logger.info("token length valid - OK");
            assertThat(token, matchesPattern("[a-zA-Z0-9]+"));
            logger.info("token matches pattern - OK");
            assertTrue(tokenSet.add(token));
            logger.info("generated token is unique - OK");
        } catch (InterruptedException e) {
            //sad but true...
            e.printStackTrace();
        }
    }

    @RepeatedTest(TOTAL_ITERATIONS)
    void testCheckToken() {
        try {
            String token = testableService.getToken();
            logger.info("got test token: " + token);
            Token tokenReturnedByRepo = new Token();
            tokenReturnedByRepo.setToken(token);
            tokenReturnedByRepo.setDate(new Date());
            Person testPerson = new Person();
            testPerson.setRole(0);
            tokenReturnedByRepo.setPerson(testPerson);
            logger.info("prepared Token obj: " + tokenReturnedByRepo);
            when(tokenRepository.findByToken(token)).thenReturn(tokenReturnedByRepo);
            assertTrue(testableService.checkToken(token));
            logger.info("check token " + tokenReturnedByRepo + " - OK");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @AfterAll
    static void invalidateTestTokenSet() {
        tokenSet = null;
    }
}
