package JavaPRO.services;

import JavaPRO.api.request.PasswordChangeRequest;
import JavaPRO.api.request.SetPasswordRequest;
import JavaPRO.api.response.OkResponse;
import JavaPRO.api.response.ResponseData;
import JavaPRO.config.Config;
import JavaPRO.config.exception.BadRequestException;
import JavaPRO.model.Person;
import JavaPRO.repository.PersonRepository;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class PassRecoveryService {

    private final EmailService emailService;
    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;
    private final String passRecoveryMessageTemplate;
    private final Logger logger;
    @Value("${spring.mail.address}")
    private String address;

    public PassRecoveryService(EmailService emailService,
                               PersonRepository personRepository,
                               PasswordEncoder passwordEncoder,
                               @Qualifier("passRecoveryLogger") Logger logger,
                               @Qualifier("PassRecoveryTemplateMessage") String passRecoveryMessageTemplate) {
        this.emailService = emailService;
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
        this.logger = logger;
        this.passRecoveryMessageTemplate = passRecoveryMessageTemplate;
    }

    public ResponseEntity<OkResponse> passRecovery(String email) throws BadRequestException {
        Person person = personRepository.findByEmail(email);
        if (person == null) {
            logger.error(String.format("Ошибка при восстановлении пароля. Пользователь с введенным email не найден. Email: %s", email));
            throw new BadRequestException(Config.STRING_AUTH_LOGIN_NO_SUCH_USER);
        }
        emailService.sendMail("Recovery password in social network",
                               String.format(passRecoveryMessageTemplate, address, person.getConfirmationCode()),
                               email);
        logger.info(String.format("Успешная отправка сообщения с ссылкой для восстановления пароля. Email: %s", email));
        return new ResponseEntity<>(new OkResponse("null", getTimestamp(), new ResponseData("OK")), HttpStatus.OK);
    }

    public ResponseEntity<OkResponse> changePassword(PasswordChangeRequest passwordChangeRequest, String userEmail) throws BadRequestException {
        String newPassword = passwordChangeRequest.getNewPassword();
        if (personRepository.changePassword(passwordEncoder.encode(newPassword), userEmail) == 1) {
            logger.info(String.format("Успешная смена пароля (Настройки пользователя). Email: %s", userEmail));
            return new ResponseEntity<>(new OkResponse("null", getTimestamp(), new ResponseData("OK")), HttpStatus.OK);
        } else {
            logger.error(String.format("Ошибка при смене пароля (Настройки пользователя). Ошибка при обработке запроса в БД. Email: %s", userEmail));
            throw new BadRequestException(Config.STRING_INVALID_SET_PASSWORD);
        }
    }

    public ResponseEntity<OkResponse> setNewPassword(SetPasswordRequest setPasswordRequest) throws BadRequestException {
        String token = setPasswordRequest.getToken();
        String password = setPasswordRequest.getPassword();
        Person person = personRepository.findByCode(token);
        if (person == null) {
            logger.error(String.format("Ошибка при смене пароля. Не найдено пользователя по предоставленному токену. Token: %s",setPasswordRequest.getToken()));
            throw new BadRequestException(Config.STRING_AUTH_LOGIN_NO_SUCH_USER);
        } else {
            if (personRepository.setNewPassword(passwordEncoder.encode(password), token) != null) {
                logger.info(String.format("Успешная смена пароля. Email: %s", person.getEmail()));
                return new ResponseEntity<>(new OkResponse("null", getTimestamp(), new ResponseData("OK")), HttpStatus.OK);
            } else {
                logger.error(String.format("Ошибка при смене пароля. Ошибка при обработке запроса в БД. Email: %s", person.getEmail()));
                logger.error("Ошибка при смене пароля. Ошибка при обработке запроса в БД. Email: " + person.getEmail());
                throw new BadRequestException(Config.STRING_INVALID_SET_PASSWORD);
            }
        }
    }

    private Long getTimestamp() {
        return (new Date().getTime() / 1000);
    }
}
