package javapro.controller;

import javapro.api.request.NotificationSetupRequest;
import javapro.api.response.PlatformResponse;
import javapro.api.response.Response;
import javapro.config.exception.AuthenticationException;
import javapro.config.exception.NotFoundException;
import javapro.services.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class NotificationController {
    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }


// todo Request URL: http://31.40.251.201:8086/api/v1/account/notifications  Request Method: GET

//  todo  PUT : http://localhost:8086/api/v1/account/notifications
//  todo GET : http://31.40.251.201:8086/api/v1/account/notifications

    @GetMapping("/api/v1/notifications")
    public ResponseEntity<PlatformResponse> getNotifications(@RequestParam(value = "offset", required = false) Long offset,
                                                             @RequestParam(value = "itemPerPage", required = false) Long itemPerPage) throws AuthenticationException, NotFoundException {
        return notificationService.getNotification(offset, itemPerPage);
    }

    @GetMapping("/api/v1/account/notifications")
    public ResponseEntity<Response> getAccountNotification() throws NotFoundException, AuthenticationException {
        return notificationService.getAccountNotification();
    }

    @PutMapping("/api/v1/account/notifications")
    public ResponseEntity<Response> setAccountNotification(@RequestBody NotificationSetupRequest request) throws AuthenticationException, NotFoundException {
        return notificationService.setAccountNotification(request);
    }

}
