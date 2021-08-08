package javapro.services;

import javapro.api.response.LoginResponse;
import javapro.api.response.OkResponse;
import javapro.api.response.ResponseData;
import javapro.config.Config;
import javapro.config.exception.BadRequestException;
import javapro.model.dto.auth.UnauthorizedPersonDTO;
import javapro.repository.DeletedPersonRepository;
import javapro.repository.PersonRepository;
import javapro.repository.TokenRepository;
import javapro.util.PersonToDtoMapper;
import javapro.util.Time;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

@Service
@EnableScheduling
public class AuthService {

    private final Logger logger;
    private final PersonRepository personRepository;
    private final AuthenticationManager authenticationManager;
    private final PersonToDtoMapper personToDtoMapper;
    private final PasswordEncoder passwordEncoder;
    private final DeletedPersonRepository deletedPersonRepository;
    private final TokenRepository tokenRepository;


    public AuthService(@Qualifier("authorizationLogger") Logger logger,
                       PersonRepository personRepository,
                       AuthenticationManager authenticationManager,
                       PersonToDtoMapper personToDtoMapper,
                       PasswordEncoder passwordEncoder,
                       DeletedPersonRepository deletedPersonRepository,
                       TokenRepository tokenRepository) {
        this.logger = logger;
        this.personRepository = personRepository;
        this.authenticationManager = authenticationManager;
        this.personToDtoMapper = personToDtoMapper;
        this.passwordEncoder = passwordEncoder;

        this.deletedPersonRepository = deletedPersonRepository;
        this.tokenRepository = tokenRepository;
    }

    public ResponseEntity<LoginResponse> loginUser(UnauthorizedPersonDTO user,
                                                   Errors validationErrors,
                                                   HttpServletRequest httpServletRequest) throws BadRequestException {



        if (validationErrors.hasErrors()) {
            logger.error(String.format("Errors in UnauthorizedPersonDTO All-errors= '%s'", validationErrors.getFieldErrors()));
            throw new BadRequestException(Config.STRING_FRONT_DATA_NOT_VALID);
        }

        final String email = user.getEmail();
        final CharSequence password = user.getPassword();

        if (email.isBlank() || password.length() == 0) {
            logger.error("Ошибка предоставленных в запросе данных. Поля Email (и)или Password пустые");
            throw new BadRequestException(Config.STRING_AUTH_EMPTY_EMAIL_OR_PASSWORD);
        }

        var userFromDB = personRepository.findByEmailForLogin(email);


        if(deletedPersonRepository.findPerson(userFromDB.getId()) != null){
            throw new BadRequestException(Config.STRING_PERSON_ISDELETED);
        }


        if (userFromDB == null) {
            logger.info(String.format("User with email '%s' is not found!", email));
            throw new BadRequestException(Config.STRING_AUTH_LOGIN_NO_SUCH_USER);
        }

        if (!passwordEncoder.matches(password, userFromDB.getPassword())) {
            logger.info(String.format("Wrong password for user with email '%s'!", email));
            throw new BadRequestException(Config.STRING_AUTH_WRONG_PASSWORD);
        }

        if (!userFromDB.isApproved() || userFromDB.isBlocked()) {
            logger.info(String.format("User with email '%s' , not approved or is blocked!", email));
            throw new BadRequestException(Config.STRING_USER_NOTAPPRUVED_OR_BLOCKED);
        }

        var authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(email, password));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        HttpSession session = httpServletRequest.getSession();
        session.setMaxInactiveInterval(-1);

        var authorizedPerson = personToDtoMapper.convertToDto(userFromDB);

        logger.info("Успешная авторизация пользователя. Email: " + email);
        return ResponseEntity
                .ok(new LoginResponse("successfully", Time.getTime(), authorizedPerson));
    }


    public ResponseEntity<OkResponse> logout(HttpServletRequest httpServletRequest) {

        HttpSession session = httpServletRequest.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return ResponseEntity.ok(new OkResponse("successfully",
                Time.getTime(),
                new ResponseData("ok")));
    }

//    @Scheduled(cron = "0 0 12 * * ?")

//    @Scheduled(cron = "0 */1 * ? * *")
    private void deleteAllNotApprovedPerson() throws InterruptedException {
        System.out.println("scheduling started");

        var time = LocalDateTime.now().minusDays(1);

        Instant instant =  time.toInstant(ZoneOffset.UTC);
        Date date = Date.from(instant);
        tokenRepository.deleteAllByDateBefore(date);
        Thread.sleep(5000);
        personRepository.deleteAllByRegDateBefore(date);
    }


}
