package JavaPRO.controller;

import JavaPRO.api.request.OnlyMailRequest;
import JavaPRO.api.request.PasswordChangeRequest;
import JavaPRO.api.request.SetPasswordRequest;
import JavaPRO.config.Config;
import JavaPRO.config.exception.BadRequestException;
import JavaPRO.config.exception.ValidationException;
import JavaPRO.services.PassRecoveryService;
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
            @ApiResponse(responseCode = "400", description = "Ошибка выполнения запроса или ошибка валидации тела запроса")})
    public ResponseEntity<?> passwordRecovery(@Valid @RequestBody OnlyMailRequest onlyMailRequest, Errors errors)
            throws ValidationException, BadRequestException {
        if (errors.hasErrors()) {
            throw new ValidationException(Config.STRING_FRONT_DATA_NOT_VALID);
        }
        return passRecoveryService.passRecovery(onlyMailRequest.getEmail());
    }

    @PutMapping(value = "/api/v1/account/password/set",
            consumes = "application/json",
            produces = "application/json")
    @Operation(description = "Установаить новый пароль")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Пароль изменен"),
            @ApiResponse(responseCode = "400", description = "Ошибка выполнения запроса или ошибка валидации тела запроса")})
    public ResponseEntity<?> passwordSet(@Valid @RequestBody SetPasswordRequest setPasswordRequest, Errors errors)
            throws BadRequestException, ValidationException {
        if (errors.hasErrors()) {
            throw new ValidationException(Config.STRING_FRONT_DATA_NOT_VALID);
        }
        return passRecoveryService.setNewPassword(setPasswordRequest);
    }

    @PutMapping(value = "/api/v1/account/password",
            consumes = "application/json",
            produces = "application/json")
    @Operation(description = "Изменение пароля в настройках пользователя")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Пароль изменен"),
            @ApiResponse(responseCode = "400", description = "Ошибка выполнения запроса или ошибка валидации тела запроса")})
    public ResponseEntity<?> passwordSet(@Valid @RequestBody PasswordChangeRequest passwordChangeRequest,
                                         Principal principal,
                                         Errors errors) throws BadRequestException, ValidationException {
        if (errors.hasErrors()) {
            throw new ValidationException(Config.STRING_FRONT_DATA_NOT_VALID);
        }
        String userEmail = principal.getName();
        if(userEmail == null) {
            throw new BadRequestException(Config.STRING_AUTH_ERROR);
        }
        return passRecoveryService.changePassword(passwordChangeRequest, userEmail);
    }
}
