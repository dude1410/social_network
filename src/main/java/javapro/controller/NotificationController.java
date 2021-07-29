package javapro.controller;

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

@CrossOrigin
@Controller
public class NotificationController {
    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/api/v1/notifications")
    public ResponseEntity<PlatformResponse<Object>> getNotifications(@RequestParam(value = "offset", required = false) Integer offset,
                                                                                         @RequestParam(value = "itemPerPage", required = false) Integer itemPerPage) throws AuthenticationException, NotFoundException {
        return notificationService.getNotification(offset, itemPerPage);
    }

    @GetMapping("/api/v1/account/notifications")
    public ResponseEntity<Response> getAccountNotification() throws NotFoundException, AuthenticationException {
        return notificationService.getAccountNotificationSetup();
    }

    @PutMapping("/api/v1/account/notifications")
    public ResponseEntity<Response<MessageDTO>> setAccountNotification(@RequestBody NotificationSetupRequest request) throws AuthenticationException, NotFoundException {
        return notificationService.setAccountNotification(request);
    }

    @PutMapping("/api/v1/notifications")
    public ResponseEntity<PlatformResponse<Object>> readNotifications (@PathVariable ("all") Boolean all,
                                                      @PathVariable("id") Integer id ){
    return notificationService.readNotifications(all, id);
    }


}
