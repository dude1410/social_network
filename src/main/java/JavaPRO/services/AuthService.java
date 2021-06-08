package JavaPRO.services;

import JavaPRO.Util.PersonToDtoMapper;
import JavaPRO.api.response.*;
import JavaPRO.config.Config;
import JavaPRO.model.DTO.Auth.UnauthorizedPersonDTO;
import JavaPRO.repository.PersonRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;



@Service
public class AuthService {
    private final Logger logger = LogManager.getLogger(AuthService.class);

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


    public ResponseEntity<Response> loginUser(UnauthorizedPersonDTO user) {

        final String email = user.getEmail();
        final CharSequence password = user.getPassword();

        var userFromDB = personRepository.findByEmailForLogin(email);

        if (userFromDB == null) {

            logger.error(String.format("User with email '%s' is not found!", email));
            return ResponseEntity
                    .badRequest()
                    .body(new ErrorResponse("e-mail not found", Config.STRING_AUTH_LOGIN_NO_SUCH_USER));
        }


        if (!passwordEncoder.matches(password, userFromDB.getPassword())) {
            logger.error(String.format("Wrong password for user with email '%s'!", email));
            return ResponseEntity
                    .badRequest()
                    .body(new ErrorResponse("password error", Config.STRING_AUTH_WRONG_PASSWORD));
        }

        if (!userFromDB.isApproved() | userFromDB.isBlocked()) {
            logger.error(String.format("User with email '%s' isBlocked or not approved!", email));
            return ResponseEntity
                    .badRequest()
                    .body(new ErrorResponse("User  email  is blocked or not approved!", Config.STRING_AUTH_LOGIN_NO_SUCH_USER));
        }

        var authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(email, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        var authorizedPerson = personToDtoMapper.convertToDto(userFromDB);

        return ResponseEntity
                .ok(new LoginResponce("successfully", new Timestamp(System.currentTimeMillis()).getTime(), authorizedPerson));
    }

    public ResponseEntity<Response> logout() {
        SecurityContextHolder.getContext().getAuthentication().setAuthenticated(false);
        logger.info(String.format("Result of logout: Is Authenticated? '%s'", SecurityContextHolder.getContext().getAuthentication().isAuthenticated()));
        if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
            return ResponseEntity
                    .badRequest()
                    .body(new ErrorResponse("invalid_request", "unsuccessfully"));
        }
        return ResponseEntity.ok(new OkResponse("successfully", new Timestamp(System.currentTimeMillis()).getTime(), new ResponseData("ok")));
    }
}
