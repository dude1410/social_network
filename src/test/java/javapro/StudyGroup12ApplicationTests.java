package javapro;

import javapro.controller.AuthController;
import javapro.controller.DefaultController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
class StudyGroup12ApplicationTests {

    private final AuthController controller;

    @MockBean
    private AuthenticationManager authenticationManagerMock;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Autowired
    public StudyGroup12ApplicationTests(AuthController controller) {
        this.controller = controller;
    }

    @Test
    void contextLoads() {
        assertNotNull(controller);
    }

}
