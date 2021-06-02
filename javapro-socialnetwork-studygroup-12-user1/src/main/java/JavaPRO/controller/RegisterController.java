package JavaPRO.controller;

import JavaPRO.api.request.MailSupportRequest;
import JavaPRO.api.request.RegisterConfirmRequest;
import JavaPRO.api.request.RegisterRequest;
import JavaPRO.api.response.Response;
import JavaPRO.services.EmailService;
import JavaPRO.services.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1")
public class RegisterController {

    private final RegisterService registerService;
    private final EmailService emailService;

    @Autowired
    public RegisterController(RegisterService registerService, EmailService emailService) {
        this.registerService = registerService;
        this.emailService = emailService;
    }

    @PostMapping(value = "/account/register", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Response> registration(@RequestBody RegisterRequest registerRequest) {
        return registerService.registerNewUser(registerRequest);
    }

    @PostMapping(value = "/account/register/confirm", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Response> registrationConfirm(@RequestBody RegisterConfirmRequest registerConfirmRequest) {
        return registerService.confirmRegistration(registerConfirmRequest);
    }

    @PostMapping("/support")
    public ResponseEntity<Response> mailSupport (@RequestBody MailSupportRequest request){
        return emailService.sendMailToSupport(request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<HttpStatus> handleException(Exception e) {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

}
