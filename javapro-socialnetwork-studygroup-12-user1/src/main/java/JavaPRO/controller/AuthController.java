package JavaPRO.controller;

import JavaPRO.api.response.Response;
import JavaPRO.model.DTO.Auth.UnauthorizedPersonDTO;
import JavaPRO.services.AuthService;
import io.swagger.annotations.Api;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value = "/api/v1/auth")
public class AuthController {

    private final AuthService authService;


    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(value = "/api/v1/auth/login",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> login(@RequestBody UnauthorizedPersonDTO user, Errors error) {
        return authService.loginUser(user, error);
    }

    @PostMapping(value = "/api/v1/auth/logout",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> logout(){
        return authService.logout();
    }
}
