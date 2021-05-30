package JavaPRO.services;

import JavaPRO.Util.PersonToDtoMapper;
import JavaPRO.api.response.ErrorResponse;
import JavaPRO.api.response.LoginResponce;
import JavaPRO.config.Config;
import JavaPRO.model.DTO.Auth.UnauthorizedPersonDTO;
import JavaPRO.repository.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import java.util.Date;


@Slf4j
@Service
public class AuthService {
    private final PersonRepository personRepository;
    private final AuthenticationManager authenticationManager;
    private final PersonToDtoMapper personToDtoMapper;
    private final PasswordEncoder passwordEncoder;


    public AuthService(PersonRepository personRepository,
                       AuthenticationManager authenticationManager,
                       PersonToDtoMapper personToDtoMapper,
                       PasswordEncoder passwordEncoder) {
        this.personRepository = personRepository;
        this.authenticationManager = authenticationManager;
        this.personToDtoMapper = personToDtoMapper;
        this.passwordEncoder = passwordEncoder;
    }


    public ResponseEntity<?> loginUser(UnauthorizedPersonDTO user, Errors validationErrors) {

        if (validationErrors.hasErrors()) {
            return ResponseEntity
                    .badRequest()
                    .body(new ErrorResponse("invalid_request", Config.STRING_AUTH_WRONG_FORMAT));
        }

        final String email = user.getEmail();
        final CharSequence password = user.getPassword();

        if (email.isBlank() || password.length() == 0)
            return ResponseEntity
                    .badRequest()
                    .body(new ErrorResponse("invalid_request", Config.STRING_AUTH_EMPTY_EMAIL_OR_PASSWORD));

        log.info(String.format("Trying to authenticate user with email '%s' ", email));

        var userFromDB = personRepository.findByEmailForLogin(email);


        if (userFromDB == null) {
            log.info(String.format("User with email '%s' is not found!", email));
            return ResponseEntity
                    .badRequest()
                    .body(new ErrorResponse("e-mail not found", Config.STRING_AUTH_LOGIN_NO_SUCH_USER));
        }
        log.info(String.format("User with email '%s' found: %s", email, userFromDB));

        if (!passwordEncoder.matches(password, userFromDB.getPassword())) {
            log.info(String.format("Wrong password for user with email '%s'!", email));
            return ResponseEntity
                    .badRequest()
                    .body(new ErrorResponse("passord error", Config.STRING_AUTH_WRONG_PASSWORD));
        }
        log.info(String.format("Correct password for user with email '%s'!", email));

        var authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(email, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        var authorizedPerson = personToDtoMapper.convertToDto(userFromDB);

        return ResponseEntity
                .ok(new LoginResponce("successfully", new Date().getTime(), authorizedPerson));
    }


}
