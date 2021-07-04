package JavaPRO.controller;

import JavaPRO.api.request.MailSupportRequest;
import JavaPRO.api.request.RegisterConfirmRequest;
import JavaPRO.api.request.RegisterRequest;
import JavaPRO.api.response.MailSupportResponse;
import JavaPRO.api.response.OkResponse;
import JavaPRO.config.Config;
import JavaPRO.config.exception.BadRequestException;
import JavaPRO.config.exception.ValidationException;
import JavaPRO.services.EmailService;
import JavaPRO.services.RegisterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "/api/v1", description = "Регистрация и обращение в службу поддержки")
public class RegisterController {

    private final RegisterService registerService;
    private final EmailService emailService;

    @Autowired
    public RegisterController(RegisterService registerService, EmailService emailService) {
        this.registerService = registerService;
        this.emailService = emailService;
    }

    @PostMapping(value = "/account/register",
            consumes = "application/json",
            produces = "application/json")
    @Operation(description = "Регистрация нового пользователя")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Ссылка на подтверждение регистрации направлена"),
            @ApiResponse(responseCode = "400", description = "На этот почтовый ящик уже зарегестрирован другой аккаунт")})
    public ResponseEntity<OkResponse> registration(@Valid @RequestBody RegisterRequest registerRequest, Errors errors) throws BadRequestException, InterruptedException, ValidationException {
        if (errors.hasErrors()) {
            throw new ValidationException(Config.STRING_FRONT_DATA_NOT_VALID);
        }
        return registerService.registerNewUser(registerRequest);
    }

    @PostMapping(value = "/account/register/confirm",
            consumes = "application/json",
            produces = "application/json")
    @Operation(description = "Подтверждение регистрации нового пользователя")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Регистрация прошла успешно"),
            @ApiResponse(responseCode = "400", description = "Неудачная попытка подтверждения регистрации")})
    public ResponseEntity<OkResponse> registrationConfirm(@Valid @RequestBody RegisterConfirmRequest registerConfirmRequest, Errors errors) throws BadRequestException, ValidationException {
        if (errors.hasErrors()) {
            throw new ValidationException(Config.STRING_FRONT_DATA_NOT_VALID);
        }
        return registerService.confirmRegistration(registerConfirmRequest);
    }

    @PostMapping("/support")
    @Operation(description = "Обращение в службу поддержки")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Обращение успешно отправлено"),
            @ApiResponse(responseCode = "404", description = "Форма обращения не заполнена или заполнена неверно")})
    public ResponseEntity<MailSupportResponse> mailSupport(@Valid @RequestBody MailSupportRequest request, Errors errors) throws BadRequestException, ValidationException {
        if (errors.hasErrors()) {
            throw new ValidationException(Config.STRING_FRONT_DATA_NOT_VALID);
        }
        return emailService.sendMailToSupport(request);
    }
}
