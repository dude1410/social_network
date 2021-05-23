package JavaPRO.controllers;

import JavaPRO.Requests.RegisterRequest;
import JavaPRO.Responses.Register.RegisterResponse;
import JavaPRO.Services.RegisterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
public class RegisterController {

    private final RegisterService registerService;

    public RegisterController(RegisterService registerService) {
        this.registerService = registerService;
    }

    @PostMapping(value = "/api/v1/account/register", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> post(@RequestBody @Validated RegisterRequest registerRequest) {
        return new ResponseEntity<>(registerService.registerNewUser(registerRequest), HttpStatus.OK);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e) {
        RegisterResponse registerResponse = new RegisterResponse("invalid_request", "bad request");
        return new ResponseEntity<>(registerResponse, HttpStatus.BAD_REQUEST);
    }

}
