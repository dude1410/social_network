package JavaPRO.controller;

import JavaPRO.api.response.ErrorResponse;
import JavaPRO.api.response.Response;
import JavaPRO.config.Config;
import JavaPRO.model.DTO.Auth.UnauthorizedPersonDTO;
import JavaPRO.services.AuthService;
import io.swagger.annotations.Api;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Api(value = "/api/v1/auth")
public class AuthController {
    private final Logger logger = LogManager.getLogger(AuthService.class);
    private final AuthService authService;


    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(value = "/api/v1/auth/login",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> login(@Valid @RequestBody UnauthorizedPersonDTO user, Errors error) {

        if (error.hasErrors()) {
            logger.error(String.format("Error in request  '%s'", error.getFieldError()));
            return ResponseEntity
                    .badRequest()
                    .body(new ErrorResponse("invalid_request", Config.STRING_USER_ISBLOCKED));
        }
        return authService.loginUser(user);
    }


    @PostMapping(value = "/api/v1/auth/logout",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> logout(){
        return authService.logout();
    }
}
