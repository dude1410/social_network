package JavaPRO.controller;

import JavaPRO.api.request.OnlyMailRequest;
import JavaPRO.api.request.SetPasswordRequest;
import JavaPRO.api.response.OkResponse;
import JavaPRO.config.exception.BadRequestException;
import JavaPRO.config.exception.NotFoundException;
import JavaPRO.services.PassRecoveryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "/api/v1/account/password/", description = "Восстановление пароля")
public class PassRecoveryController {

    private final PassRecoveryService passRecoveryService;

    public PassRecoveryController(PassRecoveryService passRecoveryService) {
        this.passRecoveryService = passRecoveryService;
    }

    @PutMapping(value = "/api/v1/account/password/recovery",
            consumes = "application/json",
            produces = "application/json")
    @Operation(description = "Запрос на восстановление пароля")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Получена ссылка на восстановление пароля"),
            @ApiResponse(responseCode = "400", description = "Почтовый ящик не передан или передан неверно"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден в БД")})
    public ResponseEntity<OkResponse> passwordRecovery(@RequestBody OnlyMailRequest onlyMailRequest) throws BadRequestException,
            NotFoundException {
        return passRecoveryService.passRecovery(onlyMailRequest.getEmail());
    }

    @PutMapping(value = "/api/v1/account/password/set",
            consumes = "application/json",
            produces = "application/json")
    @Operation(description = "Установаить новый пароль")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Пароль изменен"),
            @ApiResponse(responseCode = "400", description = "Неверный запрос, пароль не изменен"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден в БД")})
    public ResponseEntity<?> passwordSet(@RequestBody SetPasswordRequest setPasswordRequest) throws BadRequestException, NotFoundException {
        System.out.println("start recovery");
        return passRecoveryService.setNewPassword(setPasswordRequest);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<HttpStatus> handleException(Exception e) {
        System.out.println("exception");
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
