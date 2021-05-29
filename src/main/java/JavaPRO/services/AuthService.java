package JavaPRO.services;

import JavaPRO.Util.Validate;
import JavaPRO.config.Config;
import JavaPRO.model.DTO.Auth.UnauthorizedPersonDTO;
import JavaPRO.repository.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;


@Slf4j
@Service
public class AuthService {
    private final PersonRepository personRepository;
    private final AuthenticationManager authenticationManager;


    public AuthService(PersonRepository personRepository,
                       AuthenticationManager authenticationManager) {
        this.personRepository = personRepository;
        this.authenticationManager = authenticationManager;
    }


    public ResponseEntity<?> loginUser(UnauthorizedPersonDTO user, Errors validationErrors){

        if (validationErrors.hasErrors())
            return ResponseEntity.badRequest().body(Config.STRING_AUTH_ERROR);

        final String email = user.getEmail();
        final String password = user.getPassword();

        if (email.isBlank() || password.isBlank())
            return ResponseEntity.badRequest().body(Config.STRING_AUTH_EMPTY_EMAIL_OR_PASSWORD);

        log.info(String.format("Trying to authenticate user with email '%s' " +
                "and password '***'.", email));

        var userFromDB = personRepository.findByEmailForLogin(email);

        if (userFromDB == null) {
            log.info(String.format("User with email '%s' is not found!", email));
            return ResponseEntity.ok(Config.STRING_AUTH_LOGIN_NO_SUCH_USER);
        }
        log.info(String.format("User with email '%s' found: %s", email, userFromDB));

        if (!Validate.isValidPassword(password, userFromDB.getPassword())) {
            log.info(String.format("Wrong password for user with email '%s'!", email));
            return ResponseEntity.badRequest().body(Config.STRING_AUTH_WRONG_PASSWORD);
        }


        var authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(email, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        userFromDB.setPassword(null);


       return ResponseEntity.ok(userFromDB);
    }





}
