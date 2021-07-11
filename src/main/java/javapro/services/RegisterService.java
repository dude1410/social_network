package javapro.services;

import javapro.api.request.RegisterConfirmRequest;
import javapro.api.request.RegisterRequest;
import javapro.api.response.OkResponse;
import javapro.api.response.ResponseData;
import javapro.config.Config;
import javapro.config.exception.BadRequestException;
import javapro.model.Token;
import javapro.model.enums.MessagesPermission;
import javapro.model.Person;
import javapro.repository.PersonRepository;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;
import java.util.Date;

@Service
public class RegisterService {

    private final PersonRepository personRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final Logger logger;
    private final String registerMessageTemplate;
    @Value("${spring.mail.address}")
    private String address;

    public RegisterService(PersonRepository personRepository,
                           EmailService emailService,
                           PasswordEncoder passwordEncoder,
                           TokenService tokenService,
                           @Qualifier("registerLogger") Logger logger,
                           @Qualifier("RegisterTemplateMessage") String registerMessageTemplate) {
        this.personRepository = personRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
        this.logger = logger;
        this.registerMessageTemplate = registerMessageTemplate;
    }

    public ResponseEntity<OkResponse> registerNewUser(RegisterRequest userInfo)
            throws BadRequestException {
        if (personRepository.findByEmail(userInfo.getEmail()) != null){
            logger.warn(String.format("Запрос на регистрацию существующего пользователя. Email: %s", userInfo.getEmail()));
            throw new BadRequestException(Config.STRING_REPEAT_EMAIL);
        }
        else {
            Person person = addUserInDB(userInfo);
            if (person.getId() == null) {
                logger.error(String.format("Ошибка при подтверждении регистрации. " +
                        "Ошибка при добавлении нового пользователя в БД. Email: %s", person.getEmail()));
                throw new BadRequestException(Config.STRING_AUTH_LOGIN_NO_SUCH_USER);
            }
            Token token = tokenService.generatePersonToken(person);
            if (token == null) {
                logger.error(String.format("Ошибка при подтверждении регистрации. " +
                        "Ошибка генерации токена. Email: %s", person.getEmail()));
                throw new BadRequestException(Config.STRING_TOKEN_ERROR);
            }
            emailService.sendMail("Registration in social network",
                                   String.format(registerMessageTemplate, address, token.getToken()),
                                   userInfo.getEmail());
            logger.info(String.format("Успешная регистрация нового пользователя. Email: %s", userInfo.getEmail()));
            return new ResponseEntity<>(new OkResponse("null", getTimestamp(), new ResponseData("OK")),
                                        HttpStatus.OK);
        }
    }

    public ResponseEntity<OkResponse> confirmRegistration(RegisterConfirmRequest registerConfirmRequest)
            throws BadRequestException {
        Token token = tokenService.findToken(registerConfirmRequest.getToken());
        if (token == null) {
            throw new BadRequestException(Config.STRING_NO_PERSON_IN_DB);
        }
        Person person = token.getPerson();
        if (tokenService.checkToken(token.getToken()) && !person.isApproved()) {
            if (personRepository.setIsApprovedTrue(person) == 1) {
                logger.info(String.format("Подтверждение регистрации нового пользователя. Email: %s", person.getEmail()));
                return new ResponseEntity<>(new OkResponse("null", getTimestamp(), new ResponseData("OK")),
                                            HttpStatus.OK);
            }
            else {
                logger.error(String.format("Ошибка при подтверждении регистрации. " +
                        "Ошибка при обработке запроса в БД. Email: %s", person.getEmail()));
                throw new BadRequestException(Config.STRING_INVALID_CONFIRM);
            }
        }
        else {
            logger.warn(String.format("Ошибка при подтверждении регистрации. " +
                    "Истек срок действия токена или регистрация была подтверждена ранее. Email: %s", person.getEmail()));
            throw new BadRequestException(Config.STRING_INVALID_CONFIRM);
        }
    }

    private Person addUserInDB(RegisterRequest userInfo) {
        Person person = new Person();
        person.setFirstName(userInfo.getFirstName());
        person.setLastName(userInfo.getLastName());
        person.setPassword(passwordEncoder.encode(userInfo.getPasswd1()));
        person.setEmail(userInfo.getEmail());
        person.setRegDate(new Timestamp(System.currentTimeMillis()));
        person.setApproved(false);
        person.setMessagesPermission(MessagesPermission.ALL);
        person.setRole(0);
        person.setLastOnlineTime(new Timestamp(System.currentTimeMillis()));
        // TODO: 11.07.2021
        person.setConfirmationCode("token");
        personRepository.save(person);
        return person;
    }

    private Long getTimestamp(){
        return (new Date().getTime() / 1000);
    }
}
