package JavaPRO.services;

import JavaPRO.api.request.RegisterConfirmRequest;
import JavaPRO.api.request.RegisterRequest;
import JavaPRO.api.response.OkResponse;
import JavaPRO.api.response.ResponseData;
import JavaPRO.config.Config;
import JavaPRO.config.exception.BadRequestException;
import JavaPRO.model.ENUM.MessagesPermission;
import JavaPRO.model.Person;
import JavaPRO.repository.PersonRepository;
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
    private final Logger logger;
    @Value("${spring.mail.address}")
    private String address;

    public RegisterService(PersonRepository personRepository,
                           EmailService emailService,
                           PasswordEncoder passwordEncoder,
                           @Qualifier("registerLogger") Logger logger) {
        this.personRepository = personRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
        this.logger = logger;
    }

    public ResponseEntity<OkResponse> registerNewUser(RegisterRequest userInfo) throws BadRequestException {
        if (userFindInDB(userInfo.getEmail())){
            logger.warn("Запрос на регистрацию существующего пользователя. Email: " + userInfo.getEmail());
            throw new BadRequestException(Config.STRING_REPEAT_EMAIL);
        }
        else {
            String token = getToken();
            int newUserId = addUserInDB(userInfo, token);
            String messageBody = "Hello, to complete the registration follow to link " +
                    "<a href=\"" + address + "/registration/complete?userId=" +
                    newUserId + "&token=" + token + "\">Confirm registration</a>";
            emailService.sendMail("Registration in social network", messageBody, userInfo.getEmail());
            logger.info("Успешная регистрация нового пользователя. Email: " + userInfo.getEmail());
            return new ResponseEntity<>(new OkResponse("null", getTimestamp(), new ResponseData("OK")), HttpStatus.OK);
        }
    }

    public ResponseEntity<OkResponse> confirmRegistration(RegisterConfirmRequest registerConfirmRequest) throws BadRequestException {
        Integer userId = registerConfirmRequest.getUserId();
        String token = registerConfirmRequest.getToken();
        Person person = personRepository.findByIdAndCode(userId, token);
        if (personRepository.findByIdAndCode(userId, token) != null && !person.isApproved()) {
            if (personRepository.setIsApprovedTrue(userId) != null) {
                logger.info("Подтверждение регистрации нового пользователя. Email: " + person.getEmail());
                return new ResponseEntity<>(new OkResponse("null", getTimestamp(), new ResponseData("OK")), HttpStatus.OK);
            }
            else {
                logger.error("Ошибка при подтверждении регистрации. Ошибка при обработке запроса в БД. UserId: " + registerConfirmRequest.getUserId());
                throw new BadRequestException(Config.STRING_INVALID_CONFIRM);
            }
        }
        else {
            logger.warn("Ошибка при подтверждении регистрации. Пользователь не найден или регистрация была подтверждена ранее. UserId: " + registerConfirmRequest.getUserId());
            throw new BadRequestException(Config.STRING_INVALID_CONFIRM);
        }
    }

    private boolean userFindInDB(String email){
        return personRepository.findByEmail(email) != null;
    }

    private int addUserInDB(RegisterRequest userInfo, String token){
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
        person.setConfirmationCode(token);
        personRepository.save(person);
        return person.getId();
    }

    private Long getTimestamp(){
        return (new Date().getTime() / 1000);
    }

    private String getToken(){
        StringBuilder token = new StringBuilder();
        String strMass = "QWERTYUIOPASDFGHJKLZXCVBNM1234567890";
        for (int i = 0; i < 10; i++){
            token.append(strMass.charAt((int) (Math.random() * (strMass.length()))));
        }
        return passwordEncoder.encode(token.toString());
    }
}
