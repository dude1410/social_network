package JavaPRO.controllers;

import JavaPRO.api.request.RegisterConfirmRequest;
import JavaPRO.api.request.RegisterRequest;
import JavaPRO.services.RegisterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class RegisterController {

    private final RegisterService registerService;

    public RegisterController(RegisterService registerService) {
        this.registerService = registerService;
    }

    @PostMapping(value = "/api/v1/account/register", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> registration(@RequestBody @Valid RegisterRequest registerRequest) {
        return registerService.registerNewUser(registerRequest);
    }

    @PostMapping(value = "/api/v1/account/register/confirm", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> registrationConfirm(@RequestBody @Valid RegisterConfirmRequest registerConfirmRequest) {
        return registerService.confirmRegistration(registerConfirmRequest);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleException(MethodArgumentNotValidException e) {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

}
