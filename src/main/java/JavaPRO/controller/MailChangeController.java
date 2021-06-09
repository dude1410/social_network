package JavaPRO.controller;

import JavaPRO.api.request.OnlyMailRequest;
import JavaPRO.api.response.Response;
import JavaPRO.services.MailChangeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class MailChangeController {

    private final MailChangeService mailChangeService;

    public MailChangeController(MailChangeService mailChangeService) {
        this.mailChangeService = mailChangeService;
    }

    @PutMapping(value = "/api/v1/account/email", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Response> passwordRecovery(@RequestBody OnlyMailRequest onlyMailRequest) {
        String currentEmail = Principal.class.getName();
        return mailChangeService.changeMail(onlyMailRequest.getEmail(), currentEmail);
    }
}
