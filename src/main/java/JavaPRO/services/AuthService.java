package JavaPRO.services;

import JavaPRO.api.response.APIResponse;
import JavaPRO.config.Config;
import JavaPRO.model.DTO.Auth.AuthorizedUser;
import JavaPRO.model.DTO.Auth.UnauthorizedUserDTO;
import JavaPRO.model.Person;
import JavaPRO.repository.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

@Slf4j
@Service
public class AuthService {
    private final PersonRepository personRepository;
    private final AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public AuthService(PersonRepository personRepository,
                       AuthenticationManager authenticationManager) {
        this.personRepository = personRepository;
        this.authenticationManager = authenticationManager;
    }


    public ResponseEntity<?> loginUser(UnauthorizedUserDTO user, Errors validationErrors){

        if (validationErrors.hasErrors())
            return ResponseEntity.badRequest().body(APIResponse.error(Config.STRING_AUTH_ERROR));

        final String email = user.getEmail();
        final String password = user.getPassword();

        if (email.isBlank() || password.isBlank())
            return ResponseEntity.badRequest().body(APIResponse.error(
                    Config.STRING_AUTH_EMPTY_EMAIL_OR_PASSWORD));

        log.info(String.format("Trying to authenticate user with email '%s' " +
                "and password '***'.", email));

        Person userFromDB = personRepository.findByEmail(email);

        if (userFromDB == null) {
            log.info(String.format("User with email '%s' is not found!", email));
            return ResponseEntity.ok(APIResponse.error(Config.STRING_AUTH_LOGIN_NO_SUCH_USER));
        }
        log.info(String.format("User with email '%s' found: %s", email, userFromDB));

        if (!isValidPassword(password, userFromDB.getPassword())) {
            log.info(String.format("Wrong password for user with email '%s'!", email));
            return ResponseEntity.badRequest().body(APIResponse.error(Config.STRING_AUTH_WRONG_PASSWORD));
        }

        var authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(email
                        , password));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        var authorizedUser = new AuthorizedUser();
       return ResponseEntity.ok(APIResponse.ok("user", authorizedUser));
    }

    private boolean isValidPassword(String password, String hashedPassword) {
        return passwordEncoder.matches(password, hashedPassword);
    }

    @Bean
    public PasswordEncoder BCryptEncoder() {
        return new BCryptPasswordEncoder(Config.INT_AUTH_BCRYPT_STRENGTH);
    }
}
