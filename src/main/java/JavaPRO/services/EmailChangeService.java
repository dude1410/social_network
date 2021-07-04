package JavaPRO.services;

import JavaPRO.api.response.OkResponse;
import JavaPRO.api.response.ResponseData;
import JavaPRO.config.Config;
import JavaPRO.config.exception.BadRequestException;
import JavaPRO.repository.PersonRepository;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.Date;

@Service
public class EmailChangeService {

    private final PersonRepository personRepository;
    private final Logger logger;

    public EmailChangeService(PersonRepository personRepository,
                              @Qualifier("mailChangeLogger") Logger logger) {
        this.personRepository = personRepository;
        this.logger = logger;
    }

    public ResponseEntity<OkResponse> changeEmail(String oldEmail, String newEmail) throws BadRequestException {
        if (personRepository.changeEmail(oldEmail, newEmail) == 1) {
            logger.info(String.format("Успешная смена email (Настройки пользователя). old Email: %s. new Email: %s", oldEmail, newEmail));
            return new ResponseEntity<>(new OkResponse("null", getTimestamp(), new ResponseData("OK")),
                                        HttpStatus.OK);
        } else {
            logger.error(String.format("Ошибка при смене пароля (Настройки пользователя). Ошибка при обработке запроса в БД. Email: %s", oldEmail));
            throw new BadRequestException(Config.STRING_INVALID_SET_PASSWORD);
        }
    }

    private Long getTimestamp() {
        return (new Date().getTime() / 1000);
    }
}
