package javapro.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import javapro.api.request.*;
import javapro.api.response.OkResponse;
import javapro.config.Config;
import javapro.config.exception.BadRequestException;
import javapro.config.exception.ValidationException;
import javapro.services.AccountService;
import javapro.services.EmailChangeService;
import javapro.services.RegisterService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
@CrossOrigin
@RestController
@Tag(name = "/api/v1/account", description = "Обработка действия с аккаунтом")
public class AccountController {
    private final EmailChangeService emailChangeService;
    private final RegisterService registerService;
    private final AccountService accountService;

    public AccountController(EmailChangeService emailChangeService, RegisterService registerService, AccountService accountService) {
        this.emailChangeService = emailChangeService;
        this.registerService = registerService;
        this.accountService = accountService;
    }

    @PutMapping(value = "/api/v1/account/password/recovery",
            consumes = "application/json",
            produces = "application/json")
    @Operation(description = "Запрос на восстановление пароля")
    @ApiResponse(responseCode = "200", description = "Получена ссылка на восстановление пароля")
    @ApiResponse(responseCode = "400", description = "Ошибка выполнения запроса или ошибка валидации тела запроса")
    public ResponseEntity<OkResponse> passwordRecovery(@Valid @RequestBody OnlyMailRequest onlyMailRequest, Errors errors)
            throws ValidationException, BadRequestException {
        if (errors.hasErrors()) {
            throw new ValidationException(Config.STRING_FRONT_DATA_NOT_VALID);
        }
        return accountService.passRecovery(onlyMailRequest.getEmail());
    }

    @PutMapping(value = "/api/v1/account/password/set",
            consumes = "application/json",
            produces = "application/json")
    @Operation(description = "Установаить новый пароль")
    @ApiResponse(responseCode = "200", description = "Пароль изменен")
    @ApiResponse(responseCode = "400", description = "Ошибка выполнения запроса или ошибка валидации тела запроса")
    public ResponseEntity<OkResponse> passwordSet(@Valid @RequestBody SetPasswordRequest setPasswordRequest, Errors errors)
            throws BadRequestException, ValidationException {
        if (errors.hasErrors()) {
            throw new ValidationException(Config.STRING_FRONT_DATA_NOT_VALID);
        }
        return accountService.setNewPassword(setPasswordRequest);
    }

    @PutMapping(value = "/api/v1/account/password",
            consumes = "application/json",
            produces = "application/json")
    @Operation(description = "Изменение пароля в настройках пользователя")
    @ApiResponse(responseCode = "200", description = "Пароль изменен")
    @ApiResponse(responseCode = "400", description = "Ошибка выполнения запроса или ошибка валидации тела запроса")
    public ResponseEntity<OkResponse> passwordSet(@Valid @RequestBody PasswordChangeRequest passwordChangeRequest,
                                         Principal principal,
                                         Errors errors) throws BadRequestException, ValidationException {
        if (errors.hasErrors()) {
            throw new ValidationException(Config.STRING_FRONT_DATA_NOT_VALID);
        }
        String userEmail = principal.getName();
        if(userEmail == null) {
            throw new BadRequestException(Config.STRING_AUTH_ERROR);
        }
        return accountService.changePassword(passwordChangeRequest, userEmail);
    }

    @PostMapping(value = "/api/v1/account/register",
            consumes = "application/json",
            produces = "application/json")
    @Operation(description = "Регистрация нового пользователя")
    @ApiResponse(responseCode = "200", description = "Ссылка на подтверждение регистрации направлена")
    @ApiResponse(responseCode = "400", description = "На этот почтовый ящик уже зарегестрирован другой аккаунт")
    public ResponseEntity<OkResponse> registration(@Valid @RequestBody RegisterRequest registerRequest, Errors errors) throws BadRequestException, ValidationException {
        if (errors.hasErrors()) {
            throw new ValidationException(Config.STRING_FRONT_DATA_NOT_VALID);
        }
        return registerService.registerNewUser(registerRequest);
    }

    @PostMapping(value = "/api/v1/account/register/confirm",
            consumes = "application/json",
            produces = "application/json")
    @Operation(description = "Подтверждение регистрации нового пользователя")
    @ApiResponse(responseCode = "200", description = "Регистрация прошла успешно")
    @ApiResponse(responseCode = "400", description = "Неудачная попытка подтверждения регистрации")
    public ResponseEntity<OkResponse> registrationConfirm(@Valid @RequestBody RegisterConfirmRequest registerConfirmRequest, Errors errors) throws BadRequestException, ValidationException {
        if (errors.hasErrors()) {
            throw new ValidationException(Config.STRING_FRONT_DATA_NOT_VALID);
        }
        return registerService.confirmRegistration(registerConfirmRequest);
    }

    @PutMapping(value = "/api/v1/account/email",
            consumes = "application/json",
            produces = "application/json")
    @Operation(description = "Запрос на смену email (Настройки пользователя)")
    @ApiResponse(responseCode = "200", description = "Успешная смена email")
    @ApiResponse(responseCode = "400", description = "Ошибка выполнения запроса")
    public ResponseEntity<OkResponse> passwordRecovery(@Valid @RequestBody OnlyMailRequest onlyMailRequest,
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
