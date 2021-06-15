package JavaPRO.controller;

import JavaPRO.api.response.Response;
import JavaPRO.model.DTO.Auth.UnauthorizedPersonDTO;
import JavaPRO.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "/api/v1/auth", description = "Вход и выход из учетной записи")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(value = "/api/v1/auth/login",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Вход в учетную запись")
    public ResponseEntity<Response> login(@RequestBody UnauthorizedPersonDTO user,
                                          Errors error) {
        return authService.loginUser(user, error);
    }

    @PostMapping(value = "/api/v1/auth/logout",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Выход из учетной записи")
    public ResponseEntity<Response> logout(){
        return authService.logout();
    }
}
