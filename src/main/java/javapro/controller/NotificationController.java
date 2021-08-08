package javapro.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import javapro.api.request.NotificationSetupRequest;
import javapro.api.response.PlatformResponse;
import javapro.api.response.Response;
import javapro.config.exception.AuthenticationException;
import javapro.config.exception.NotFoundException;
import javapro.model.dto.MessageDTO;
import javapro.services.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Tag(name = "/api/v1/notifications", description = "Работа с уведомлениями")
@CrossOrigin
@Controller
public class NotificationController {
    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @ApiResponses({@ApiResponse(responseCode = "200", description = "Список уведомлений получен"),
            @ApiResponse(responseCode = "404", description = "Список уведомлений пуст"),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован")})
    @Operation(description = "Получение списка уведомлений")
    @GetMapping("/api/v1/notifications")
    public ResponseEntity<PlatformResponse<Object>> getNotifications(@RequestParam(value = "offset", required = false) Integer offset,
                                                                                         @RequestParam(value = "itemPerPage", required = false) Integer itemPerPage) throws AuthenticationException, NotFoundException {
        return notificationService.getNotification(offset, itemPerPage);
    }

    @ApiResponses({@ApiResponse(responseCode = "200", description = "Настройки уведомлений получены"),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован")})
    @Operation(description = "Получение настроек уведомлений пользователя")
    @GetMapping("/api/v1/account/notifications")
    public ResponseEntity<Response<Object>> getAccountNotification() throws NotFoundException, AuthenticationException {
        return notificationService.getAccountNotificationSetup();
    }

    @ApiResponses({@ApiResponse(responseCode = "200", description = "Настройка изменена"),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован")})
    @Operation(description = "Установка настроек уведомлений пользователя")
    @PutMapping("/api/v1/account/notifications")
    public ResponseEntity<Response<MessageDTO>> setAccountNotification(@RequestBody NotificationSetupRequest request) throws AuthenticationException, NotFoundException {
        return notificationService.setAccountNotification(request);
    }

    @ApiResponses({@ApiResponse(responseCode = "200", description = "Уведомление прочитано"),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован")})
    @Operation(description = "Прочтение единичного уведомления пользователя")
    @DeleteMapping("/api/v1/notifications/{id}")
    public ResponseEntity<PlatformResponse<Object>> readNotifications (@PathVariable("id") Integer id ) throws NotFoundException {
    return notificationService.readNotifications(id);
    }

    @ApiResponses({@ApiResponse(responseCode = "200", description = "Все уведомления прочитаны"),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован")})
    @Operation(description = "Прочтение всех уведомлений пользователя")
    @DeleteMapping("/api/v1/notifications")
    public ResponseEntity<PlatformResponse<Object>> readAllNotifications (){
        return notificationService.readAllNotifications();
    }

}
