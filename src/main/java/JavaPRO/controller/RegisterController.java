package JavaPRO.controller;

import JavaPRO.api.request.RegisterConfirmRequest;
import JavaPRO.api.request.RegisterRequest;
import JavaPRO.api.response.Response;
import JavaPRO.services.RegisterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;


@RestController
public class RegisterController {

    private final RegisterService registerService;

    public RegisterController(RegisterService registerService) {
        this.registerService = registerService;
    }

    @PostMapping(value = "/api/v1/account/register", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Response> registration(@RequestBody RegisterRequest registerRequest) {
        return registerService.registerNewUser(registerRequest);
    }

    @PostMapping(value = "/api/v1/account/register/confirm", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Response> registrationConfirm(@RequestBody RegisterConfirmRequest registerConfirmRequest) {
        return registerService.confirmRegistration(registerConfirmRequest);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<HttpStatus> handleException(Exception e) {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

}
