package JavaPRO.controller;

import JavaPRO.api.request.OnlyMailRequest;
import JavaPRO.api.request.PasswordChangeRequest;
import JavaPRO.api.request.SetPasswordRequest;
import JavaPRO.api.response.OkResponse;
import JavaPRO.config.Config;
import JavaPRO.config.exception.BadRequestException;
import JavaPRO.services.PassRecoveryService;
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
@Tag(name = "/api/v1/account/password/", description = "Обработка запросов связанных с восстановлением и сменой пароля")
public class PassRecoveryController {

    private final PassRecoveryService passRecoveryService;

    public PassRecoveryController(PassRecoveryService passRecoveryService) {
        this.passRecoveryService = passRecoveryService;
    }

    @PutMapping(value = "/api/v1/account/password/recovery",
            consumes = "application/json",
            produces = "application/json")
    @Operation(description = "Запрос на восстановление пароля")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Получена ссылка на восстановление пароля"),
            @ApiResponse(responseCode = "400", description = "Ошибка выполнения запроса")})
    public ResponseEntity<OkResponse> passwordRecovery(@RequestBody OnlyMailRequest onlyMailRequest) throws BadRequestException {
        return passRecoveryService.passRecovery(onlyMailRequest.getEmail());
    }

    @PutMapping(value = "/api/v1/account/password/set",
            consumes = "application/json",
            produces = "application/json")
    @Operation(description = "Установаить новый пароль")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Пароль изменен"),
            @ApiResponse(responseCode = "400", description = "Ошибка выполнения запроса")})
    public ResponseEntity<?> passwordSet(@RequestBody SetPasswordRequest setPasswordRequest) throws BadRequestException {
        return passRecoveryService.setNewPassword(setPasswordRequest);
    }

    @PutMapping(value = "/api/v1/account/password",
            consumes = "application/json",
            produces = "application/json")
    @Operation(description = "Изменение пароля в настройках пользователя")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Пароль изменен"),
            @ApiResponse(responseCode = "400", description = "Ошибка выполнения запроса")})
    public ResponseEntity<?> passwordSet(@RequestBody PasswordChangeRequest passwordChangeRequest, Principal principal) throws BadRequestException {
        String userEmail = principal.getName();
        if(userEmail == null) {
            throw new BadRequestException(Config.STRING_AUTH_ERROR);
        }
        return passRecoveryService.changePassword(passwordChangeRequest, userEmail);
    }
}
