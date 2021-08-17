package javapro.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import javapro.model.dto.auth.UnauthorizedPersonDTO;
import org.junit.Ignore;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.yaml")
@ActiveProfiles(value = "test")
class AuthControllerTest {

    private final MockMvc mockMvc;
    private static UnauthorizedPersonDTO validTestUser;
    private static UnauthorizedPersonDTO invalidTestUser;
    private final ObjectMapper mapper;

    @MockBean
    AuthenticationManager authenticationManager;

    @MockBean
    PasswordEncoder passwordEncoder;

    @Autowired
    public AuthControllerTest(ObjectMapper mapper, WebApplicationContext webApplicationContext) {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
        this.mapper = mapper;
    }

    @BeforeAll
    static void setUpLoginPayload() {
        validTestUser = new UnauthorizedPersonDTO();
        validTestUser.setEmail("prokhorovoleg1977@gmail.com");
        validTestUser.setPassword("Jktu1977");

        invalidTestUser = new UnauthorizedPersonDTO();
        invalidTestUser.setEmail("anonimous@mail.com");
        invalidTestUser.setPassword("blabla1123");
    }

    @Test
    void testInvalidUserLogin() throws Exception {
        String requestBody = mapper.writer().withDefaultPrettyPrinter().writeValueAsString(invalidTestUser);
        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
                .characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @Ignore
    void testValidUserLogin() throws Exception {
        String requestBody = mapper.writer().withDefaultPrettyPrinter().writeValueAsString(validTestUser);
        mockMvc.perform(post("/api/v1/auth/login")
                .with(csrf().asHeader())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
                .characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("prokhorovoleg1977@gmail.com")
    void logout() throws Exception {
        mockMvc.perform(post("/api/v1/auth/logout")
                .with(csrf().asHeader()))
                .andDo(print())
                .andExpect(status().isOk());
    }

}
