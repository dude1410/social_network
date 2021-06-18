package JavaPRO.services;

import JavaPRO.Util.PersonToDtoMapper;
import JavaPRO.api.response.*;
import JavaPRO.config.Config;
import JavaPRO.config.exception.BadRequestException;
import JavaPRO.controller.LoggingController;
import JavaPRO.model.DTO.Auth.UnauthorizedPersonDTO;
import JavaPRO.repository.PersonRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import java.sql.Timestamp;

@Service
@EnableScheduling
public class AuthService {
    private final Logger logger = LogManager.getLogger(LoggingController.class);

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

    public ResponseEntity<LoginResponse> loginUser(UnauthorizedPersonDTO user,
                                              Errors validationErrors) throws BadRequestException {



        logger.error(String.format("Errors in UnauthorizedPersonDTO All-errors= '%s'", validationErrors.getFieldErrors()));

        if (validationErrors.hasErrors()) {
            throw new BadRequestException(Config.STRING_FRONT_DATA_NOT_VALID);
        }

        final String email = user.getEmail();
        final CharSequence password = user.getPassword();

        if (email.isBlank() || password.length() == 0) {
            throw new BadRequestException(Config.STRING_AUTH_EMPTY_EMAIL_OR_PASSWORD);
        }

        var userFromDB = personRepository.findByEmailForLogin(email);

        if (userFromDB == null) {
            logger.info(String.format("User with email '%s' is not found!", email));
            throw new BadRequestException(Config.STRING_AUTH_LOGIN_NO_SUCH_USER);
        }

        if (!passwordEncoder.matches(password, userFromDB.getPassword())) {
            logger.info(String.format("Wrong password for user with email '%s'!", email));
            throw new BadRequestException(Config.STRING_AUTH_WRONG_PASSWORD);
        }

        if(!userFromDB.isApproved() || userFromDB.isBlocked()){
            logger.info(String.format("User with email '%s' , not approved or is blocked!", email));
            throw new BadRequestException(Config.STRING_USER_NOTAPPRUVED_OR_BLOCKED);
        }

        var authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(email, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        var authorizedPerson = personToDtoMapper.convertToDto(userFromDB);

        return ResponseEntity
                .ok(new LoginResponse("successfully", new Timestamp(System.currentTimeMillis()).getTime(), authorizedPerson));
    }

    public ResponseEntity<OkResponse> logout() throws BadRequestException {
        SecurityContextHolder.getContext().getAuthentication().setAuthenticated(false);
        logger.info(String.format("Result of logout: Is Authenticated? '%s'",
                SecurityContextHolder.getContext().getAuthentication().isAuthenticated()));
        if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
            throw new BadRequestException(Config.STRING_LOGOUT_UNSUCCESSFUL);
        }
        return ResponseEntity.ok(new OkResponse("successfully",
                new Timestamp(System.currentTimeMillis()).getTime(),
                new ResponseData("ok")));
    }

    @Scheduled(cron = "0 0 12 * * ?")
    private void deleteAllNotApprovedPerson(){
        var time = new Timestamp(new Timestamp(System.currentTimeMillis()).getTime() - 86400000);
        personRepository.deleteAllByRegDateBefore(time);
    }
}
