package socnet.unit_tests.api.services;

import javapro.services.TokenService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.RepeatedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.matchesPattern;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = {TokenService.class})
class TokenServiceTest {

    private final int TOTAL_ITERATIONS = 500;
    private final int MAX_TOKEN_LENGTH = 50;
    private final TokenService testableService;
    private static HashSet<String> tokenSet;

    @Autowired
    public TokenServiceTest(TokenService service) {
        this.testableService = service;
    }

    @BeforeAll
    static void setUpGetTokenTest() {
        tokenSet = new HashSet<>();
    }


    @RepeatedTest(TOTAL_ITERATIONS)
    void testGetRandomString() {
        try {
            String token = testableService.getToken();
            assertNotNull(token);
            assertTrue(token.length() > 0 && token.length() <= MAX_TOKEN_LENGTH);
            assertThat(token, matchesPattern("[a-zA-Z0-9]+"));
            assertTrue(tokenSet.add(token));
        } catch (InterruptedException e) {
            //sad but true...
            e.printStackTrace();
        }
    }

    @RepeatedTest(TOTAL_ITERATIONS)
    void testCheckToken(){
        try {
            String token = testableService.getToken();
            assertTrue(testableService.checkToken(token));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @AfterAll
    static void invalidateTestTokenSet(){
        tokenSet = null;
    }
}
