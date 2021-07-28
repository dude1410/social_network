package javapro.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import javapro.api.request.MailSupportRequest;
import javapro.api.response.MailSupportResponse;
import javapro.config.Config;
import javapro.config.exception.BadRequestException;
import javapro.config.exception.ValidationException;
import javapro.services.EmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "/api/v1", description = "Регистрация и обращение в службу поддержки")
public class RegisterController {

    private final EmailService emailService;

    public RegisterController(EmailService emailService) {
        this.emailService = emailService;
    }


    @PostMapping("/support")
    @Operation(description = "Обращение в службу поддержки")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Обращение успешно отправлено"),
            @ApiResponse(responseCode = "404", description = "Форма обращения не заполнена или заполнена неверно")})
    public ResponseEntity<MailSupportResponse> mailSupport(@Valid @RequestBody MailSupportRequest request, Errors errors) throws ValidationException {
        if (errors.hasErrors()) {
            throw new ValidationException(Config.STRING_FRONT_DATA_NOT_VALID);
        }
        return emailService.sendMailToSupport(request);
    }
}
