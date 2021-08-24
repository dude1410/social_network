package javapro.services;

import javapro.api.response.LoginResponse;
import javapro.api.response.OkResponse;
import javapro.api.response.ResponseData;
import javapro.config.Config;
import javapro.config.exception.BadRequestException;
import javapro.config.exception.NotFoundException;
import javapro.model.Person;
import javapro.model.dto.auth.UnauthorizedPersonDTO;
import javapro.repository.DeletedPersonRepository;
import javapro.repository.NotificationSetupRepository;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
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
    private final NotificationSetupRepository notificationSetupRepository;


    public AuthService(@Qualifier("authorizationLogger") Logger logger,
                       PersonRepository personRepository,
                       AuthenticationManager authenticationManager,
                       PersonToDtoMapper personToDtoMapper,
                       PasswordEncoder passwordEncoder,
                       DeletedPersonRepository deletedPersonRepository,
                       TokenRepository tokenRepository,
                       NotificationSetupRepository notificationSetupRepository) {
        this.logger = logger;
        this.personRepository = personRepository;
        this.authenticationManager = authenticationManager;
        this.personToDtoMapper = personToDtoMapper;
        this.passwordEncoder = passwordEncoder;

        this.deletedPersonRepository = deletedPersonRepository;
        this.tokenRepository = tokenRepository;
        this.notificationSetupRepository = notificationSetupRepository;
    }



    public ResponseEntity<LoginResponse> loginUser(UnauthorizedPersonDTO user,
                                                   HttpServletRequest httpServletRequest) throws BadRequestException, NotFoundException {


        final String email = user.getEmail();
        final CharSequence password = user.getPassword();

        if (email.isBlank() || password.length() == 0) {
            logger.error("Поля Email (и)или Password пустые");
            throw new BadRequestException(Config.STRING_AUTH_EMPTY_EMAIL_OR_PASSWORD);
        }

        var userFromDB = personRepository.findByEmailForLogin(email);

        if(userFromDB == null){
            logger.info("User with email {} is not found!", email);
            throw new NotFoundException(Config.STRING_NO_PERSON_IN_DB);
        }

        if(deletedPersonRepository.findPerson(userFromDB.getId()) != null){
            throw new BadRequestException(Config.STRING_PERSON_ISDELETED);
        }

        if (!passwordEncoder.matches(password, userFromDB.getPassword())) {
            logger.info("Wrong password for user with email {}!", email);
            throw new BadRequestException(Config.STRING_AUTH_WRONG_PASSWORD);
        }

        if (!userFromDB.isApproved() || userFromDB.isBlocked()) {
            logger.info("User with email {} , not approved or is blocked!", email);
            throw new BadRequestException(Config.STRING_USER_NOTAPPRUVED_OR_BLOCKED);
        }
        userFromDB.setLastOnlineTime(new Timestamp(Time.getTime()));
        personRepository.save(userFromDB);

        var authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(email, password));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        HttpSession session = httpServletRequest.getSession();
        session.setMaxInactiveInterval(-1);

        var authorizedPerson = personToDtoMapper.convertToDto(userFromDB);

        logger.info("Успешная авторизация пользователя. Email: {} ", email);
        return ResponseEntity
                .ok(new LoginResponse(Config.WALL_RESPONSE, Time.getTime(), authorizedPerson));
    }



    public ResponseEntity<OkResponse> logout(HttpServletRequest httpServletRequest) {

        HttpSession session = httpServletRequest.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return ResponseEntity.ok(new OkResponse(Config.WALL_RESPONSE,
                Time.getTime(),
                new ResponseData("ok")));
    }

//    @Scheduled(cron = "0 0 12 * * ?")

    @Scheduled(cron = "0 */1 * ? * *")
    private void deleteAllNotApprovedPerson() throws InterruptedException {
        var time = LocalDateTime.now().minusDays(1);
        var instant =  time.toInstant(ZoneOffset.UTC);
        var date = Date.from(instant);
        tokenRepository.deleteAllByDateBefore(date);
        Thread.sleep(5000);
        var listNotApprovedPerson = personRepository.findAllByRegDateBefore(date);
        for(Person element: listNotApprovedPerson ){
            notificationSetupRepository.deleteNotificationSetupsByPersonId(element.getId());
        }
        personRepository.deleteAllByRegDateBefore(date);
    }


}
