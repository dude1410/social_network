package javapro.services;

import javapro.api.request.PasswordChangeRequest;
import javapro.api.request.SetPasswordRequest;
import javapro.api.response.OkResponse;
import javapro.api.response.ResponseData;
import javapro.config.Config;
import javapro.config.exception.BadRequestException;
import javapro.repository.DeletedPersonRepository;
import javapro.repository.PersonRepository;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AccountService {

    private final EmailService emailService;
    private final TokenService tokenService;
    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;
    private final String passRecoveryMessageTemplate;
    private final DeletedPersonRepository deletedPersonRepository;
    private final Logger logger;
    @Value("${spring.mail.address}")
    private String address;

    public AccountService(EmailService emailService,
                          TokenService tokenService, PersonRepository personRepository,
                          PasswordEncoder passwordEncoder,
                          @Qualifier("passRecoveryLogger") Logger logger,
                          @Qualifier("PassRecoveryTemplateMessage") String passRecoveryMessageTemplate,
                          DeletedPersonRepository deletedPersonRepository) {
        this.emailService = emailService;
        this.tokenService = tokenService;
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
        this.logger = logger;
        this.passRecoveryMessageTemplate = passRecoveryMessageTemplate;
        this.deletedPersonRepository = deletedPersonRepository;
    }

    public ResponseEntity<OkResponse> passRecovery(String email) throws BadRequestException {
        var person = personRepository.findByEmail(email);

        if (person == null) {
            logger.error("Ошибка при восстановлении пароля. Пользователь с введенным email не найден. Email: {}", email);
            throw new BadRequestException(Config.STRING_AUTH_LOGIN_NO_SUCH_USER);
        }
        if (deletedPersonRepository.findPerson(person.getId()) != null){
            throw new BadRequestException(Config.STRING_PERSON_ISDELETED);
        }
        var newToken = tokenService.setNewPersonToken(person);
        emailService.sendMail("Recovery password in social network",
                               String.format(passRecoveryMessageTemplate, address, newToken.getToken()),
                               email);
        logger.info("Успешная отправка сообщения с ссылкой для восстановления пароля. Email: {}", email);
        return new ResponseEntity<>(new OkResponse("null", getTimestamp(), new ResponseData("OK")), HttpStatus.OK);
    }

    public ResponseEntity<OkResponse> changePassword(PasswordChangeRequest passwordChangeRequest, String userEmail) throws BadRequestException {
        var newPassword = passwordChangeRequest.getNewPassword();
        if (personRepository.changePassword(passwordEncoder.encode(newPassword), userEmail) == 1) {
            logger.info("Успешная смена пароля (Настройки пользователя). Email: {}", userEmail);
            return new ResponseEntity<>(new OkResponse("null", getTimestamp(), new ResponseData("OK")), HttpStatus.OK);
        } else {
            logger.error("Ошибка при смене пароля (Настройки пользователя). " +
                    "Ошибка при обработке запроса в БД. Email: {}", userEmail);
            throw new BadRequestException(Config.STRING_INVALID_SET_PASSWORD);
        }
    }

    public ResponseEntity<OkResponse> setNewPassword(SetPasswordRequest setPasswordRequest) throws BadRequestException {
        var password = setPasswordRequest.getPassword();
        var token = tokenService.findToken(setPasswordRequest.getToken());
        if (token == null || !tokenService.checkToken(token.getToken())) {
            logger.error("Ошибка при смене пароля. Ошибка проверки токена. Token: {}", setPasswordRequest.getToken());
            throw new BadRequestException(Config.STRING_TOKEN_CHECK_ERROR);
        } else {
            var person = token.getPerson();

            if (personRepository.setNewPassword(passwordEncoder.encode(password), person) != null) {
                logger.info("Успешная смена пароля. Email: {}", person.getEmail());
                return new ResponseEntity<>(new OkResponse("null", getTimestamp(), new ResponseData("OK")), HttpStatus.OK);
            } else {
                logger.error("Ошибка при смене пароля. " +
                        "Ошибка при обработке запроса в БД. Email: {}", person.getEmail());
                throw new BadRequestException(Config.STRING_INVALID_SET_PASSWORD);
            }
        }
    }

    private Long getTimestamp() {
        return (new Date().getTime() / 1000);
    }
}
