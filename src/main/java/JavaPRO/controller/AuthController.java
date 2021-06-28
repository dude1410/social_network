package JavaPRO.controller;

import JavaPRO.api.response.OkResponse;
import JavaPRO.api.response.Response;
import JavaPRO.config.exception.BadRequestException;
import JavaPRO.model.DTO.Auth.UnauthorizedPersonDTO;
import JavaPRO.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Успешная попытка зайти в учетную запись"),
            @ApiResponse(responseCode = "400", description = "Полученные данные не прошли валидацию")})
    public ResponseEntity<Response> login(@RequestBody UnauthorizedPersonDTO user,
                                          Errors error) throws BadRequestException {
        return authService.loginUser(user, error);
    }

    @PostMapping(value = "/api/v1/auth/logout",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Выход из учетной записи")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Успешная попытка выйти из учетной записи"),
            @ApiResponse(responseCode = "400", description = "Неуспешная попытка выйти из учетной записи")})
    public ResponseEntity<OkResponse> logout() throws BadRequestException {
        return authService.logout();
    }
}
