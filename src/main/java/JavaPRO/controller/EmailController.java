package JavaPRO.controller;

import JavaPRO.api.request.OnlyMailRequest;
import JavaPRO.config.Config;
import JavaPRO.config.exception.BadRequestException;
import JavaPRO.services.EmailChangeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<?> passwordRecovery(@RequestBody OnlyMailRequest onlyMailRequest, Principal principal) throws BadRequestException {
        String userEmail = principal.getName();
        if(principal.getName() == null) {
            throw new BadRequestException(Config.STRING_AUTH_ERROR);
        }
        return emailChangeService.changeEmail(userEmail, onlyMailRequest.getEmail());
    }
}
