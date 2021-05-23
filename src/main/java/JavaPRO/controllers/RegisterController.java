package JavaPRO.controllers;

import JavaPRO.api.request.RegisterRequest;
import JavaPRO.api.response.RegisterErrorResponse;
import JavaPRO.services.RegisterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class RegisterController {

    private final RegisterService registerService;

    public RegisterController(RegisterService registerService) {
        this.registerService = registerService;
    }

    @PostMapping(value = "/api/v1/account/register", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> post(@RequestBody RegisterRequest registerRequest) {
        return new ResponseEntity<>(registerService.registerNewUser(registerRequest), HttpStatus.OK);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e) {
        return new ResponseEntity<>(new RegisterErrorResponse("invalid_request", "error in response process"), HttpStatus.BAD_REQUEST);
    }

}
