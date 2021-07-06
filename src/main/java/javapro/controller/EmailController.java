package javapro.controller;

import javapro.api.request.OnlyMailRequest;
import javapro.config.Config;
import javapro.config.exception.BadRequestException;
import javapro.config.exception.ValidationException;
import javapro.services.EmailChangeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;
import java.security.Principal;

@RestController
@Tag(name = "/api/v1/account/email", description = "Смена email в настройках пользователя")
public class EmailController {

    private final EmailChangeService emailChangeService;

    public EmailController(EmailChangeService emailChangeService) {
        this.emailChangeService = emailChangeService;
    }

    @PutMapping(value = "/api/v1/account/email",
            consumes = "application/json",
            produces = "application/json")
    @Operation(description = "Запрос на смену email (Настройки пользователя)")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Успешная смена email"),
            @ApiResponse(responseCode = "400", description = "Ошибка выполнения запроса")})
    public ResponseEntity<?> passwordRecovery(@Valid @RequestBody OnlyMailRequest onlyMailRequest,
                                              Principal principal,
                                              Errors errors) throws BadRequestException, ValidationException {
        if (errors.hasErrors()) {
            throw new ValidationException(Config.STRING_FRONT_DATA_NOT_VALID);
        }
        if(principal == null) {
            throw new BadRequestException(Config.STRING_AUTH_ERROR);
        }
        String userEmail = principal.getName();
        return emailChangeService.changeEmail(userEmail, onlyMailRequest.getEmail());
    }
}
