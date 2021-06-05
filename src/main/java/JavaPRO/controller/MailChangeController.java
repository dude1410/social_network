package JavaPRO.controller;

import JavaPRO.api.request.OnlyMailRequest;
import JavaPRO.api.response.Response;
import JavaPRO.services.MailChangeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ExceptionHandler;
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
        System.out.println("get request " + SecurityContextHolder.getContext().getAuthentication().getName());
        String mail = SecurityContextHolder.getContext().getAuthentication().getName();
        return mailChangeService.changeMail(onlyMailRequest.getEmail(), mail);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<HttpStatus> handleException(Exception e) {
        System.out.println("exception" + e.getMessage());
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
