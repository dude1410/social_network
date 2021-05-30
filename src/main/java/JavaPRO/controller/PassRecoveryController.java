package JavaPRO.controller;

import JavaPRO.api.request.OnlyMailRequest;
import JavaPRO.api.response.ErrorResponse;
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
    public ResponseEntity<?> post(@RequestBody OnlyMailRequest onlyMailRequest) {
        System.out.println("start recovery");
        return new ResponseEntity<>(passRecoveryService.passRecovery(onlyMailRequest.getEmail()), HttpStatus.OK);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e) {
        return new ResponseEntity<>(new ErrorResponse("invalid_request", "error in response process"), HttpStatus.BAD_REQUEST);
    }
}
