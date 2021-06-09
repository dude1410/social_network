package JavaPRO.controller;

import JavaPRO.api.request.OnlyMailRequest;
import JavaPRO.api.request.SetPasswordRequest;
import JavaPRO.api.response.ErrorResponse;
import JavaPRO.api.response.Response;
import JavaPRO.services.PassRecoveryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
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
    public ResponseEntity<Response> passwordRecovery(@Validated(OnlyMailRequest.class) @RequestBody OnlyMailRequest onlyMailRequest, Errors errors) {
        if (errors.hasErrors()){
            return new ResponseEntity<>(new ErrorResponse("invalid_request", "password recovery error"), HttpStatus.BAD_REQUEST);
        }
        return passRecoveryService.passRecovery(onlyMailRequest.getEmail());
    }

    @PutMapping(value = "/api/v1/account/password/set", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> passwordSet(@RequestBody SetPasswordRequest setPasswordRequest, Errors errors) {
        if (errors.hasErrors()){
            return new ResponseEntity<>(new ErrorResponse("invalid_request", "password set error"), HttpStatus.BAD_REQUEST);
        }
        return passRecoveryService.setNewPassword(setPasswordRequest);
    }
}
