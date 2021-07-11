package javapro.services;

import javapro.api.request.PasswordChangeRequest;
import javapro.api.request.SetPasswordRequest;
import javapro.api.response.OkResponse;
import javapro.api.response.ResponseData;
import javapro.config.Config;
import javapro.config.exception.BadRequestException;
import javapro.model.Person;
import javapro.model.Token;
import javapro.repository.PersonRepository;
import javapro.repository.TokenRepository;
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
    private final TokenService tokenService;
    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;
    private final String passRecoveryMessageTemplate;
    private final Logger logger;
    @Value("${spring.mail.address}")
    private String address;

    public PassRecoveryService(EmailService emailService,
                               TokenService tokenService, PersonRepository personRepository,
                               TokenRepository tokenRepository, PasswordEncoder passwordEncoder,
                               @Qualifier("passRecoveryLogger") Logger logger,
                               @Qualifier("PassRecoveryTemplateMessage") String passRecoveryMessageTemplate) {
        this.emailService = emailService;
        this.tokenService = tokenService;
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
        Token newToken = tokenService.setNewPersonToken(person);
        emailService.sendMail("Recovery password in social network",
                               String.format(passRecoveryMessageTemplate, address, newToken.getToken()),
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
            logger.error(String.format("Ошибка при смене пароля (Настройки пользователя). " +
                    "Ошибка при обработке запроса в БД. Email: %s", userEmail));
            throw new BadRequestException(Config.STRING_INVALID_SET_PASSWORD);
        }
    }

    public ResponseEntity<OkResponse> setNewPassword(SetPasswordRequest setPasswordRequest) throws BadRequestException {
        String password = setPasswordRequest.getPassword();
        Token token = tokenService.findToken(setPasswordRequest.getToken());
        if (token == null || !tokenService.checkToken(token.getToken())) {
            logger.error(String.format("Ошибка при смене пароля. Ошибка проверки токена. Token: %s", setPasswordRequest.getToken()));
            throw new BadRequestException(Config.STRING_TOKEN_CHECK_ERROR);
        } else {
            Person person = token.getPerson();
            if (personRepository.setNewPassword(passwordEncoder.encode(password), person) != null) {
                logger.info(String.format("Успешная смена пароля. Email: %s", person.getEmail()));
                return new ResponseEntity<>(new OkResponse("null", getTimestamp(), new ResponseData("OK")), HttpStatus.OK);
            } else {
                logger.error(String.format("Ошибка при смене пароля. " +
                        "Ошибка при обработке запроса в БД. Email: %s", person.getEmail()));
                throw new BadRequestException(Config.STRING_INVALID_SET_PASSWORD);
            }
        }
    }

    private Long getTimestamp() {
        return (new Date().getTime() / 1000);
    }
}
