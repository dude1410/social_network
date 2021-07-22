package javapro.services;

import javapro.api.response.LoginResponse;
import javapro.api.response.OkResponse;
import javapro.api.response.ResponseData;
import javapro.config.Config;
import javapro.config.exception.BadRequestException;
import javapro.model.dto.auth.UnauthorizedPersonDTO;
import javapro.repository.DeletedPersonRepository;
import javapro.repository.PersonRepository;
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
import java.sql.Timestamp;

@Service
@EnableScheduling
public class AuthService {

    private final Logger logger;
    private final PersonRepository personRepository;
    private final AuthenticationManager authenticationManager;
    private final PersonToDtoMapper personToDtoMapper;
    private final PasswordEncoder passwordEncoder;
    private final DeletedPersonRepository deletedPersonRepository;


    public AuthService(@Qualifier("authorizationLogger") Logger logger,
                       PersonRepository personRepository,
                       AuthenticationManager authenticationManager,
                       PersonToDtoMapper personToDtoMapper,
                       PasswordEncoder passwordEncoder,
                       DeletedPersonRepository deletedPersonRepository) {
        this.logger = logger;
        this.personRepository = personRepository;
        this.authenticationManager = authenticationManager;
        this.personToDtoMapper = personToDtoMapper;
        this.passwordEncoder = passwordEncoder;

        this.deletedPersonRepository = deletedPersonRepository;
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

        if(deletedPersonRepository.findByPersonId(userFromDB.getId()).isPresent()){
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

    @Scheduled(cron = "0 0 12 * * ?")
    private void deleteAllNotApprovedPerson() {
        var time = new Timestamp(Time.getTime() - 86400000);
        personRepository.deleteAllByRegDateBefore(time);
    }


}
