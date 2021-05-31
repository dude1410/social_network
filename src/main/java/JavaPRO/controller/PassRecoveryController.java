package JavaPRO.controller;

import JavaPRO.api.request.OnlyMailRequest;
import JavaPRO.api.request.SetPasswordRequest;
import JavaPRO.api.response.Response;
import JavaPRO.services.PassRecoveryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class PassRecoveryController {

    private final PassRecoveryService passRecoveryService;

    public PassRecoveryController(PassRecoveryService passRecoveryService) {
        this.passRecoveryService = passRecoveryService;
    }

    @PutMapping(value = "/api/v1/account/password/recovery", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Response> passwordRecovery(@RequestBody OnlyMailRequest onlyMailRequest) {
        return passRecoveryService.passRecovery(onlyMailRequest.getEmail());
    }

    @PutMapping(value = "/api/v1/account/password/set", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> passwordSet(@RequestBody SetPasswordRequest setPasswordRequest) {
        System.out.println("start recovery");
        return passRecoveryService.setNewPassword(setPasswordRequest);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<HttpStatus> handleException(Exception e) {
        System.out.println("exception");
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
