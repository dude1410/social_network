package javapro.controller;

import javapro.api.response.PlatformResponse;
import javapro.services.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class NotificationController {
    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }



//  todo  PUT : http://localhost:8086/api/v1/account/notifications


    @GetMapping("/api/v1/notifications")
    public ResponseEntity<PlatformResponse> getNotifications(@RequestParam(value = "offset", required = false) Long offset,
                                                             @RequestParam(value = "itemPerPage", required = false) Long itemPerPage) {

        return notificationService.getNotification(offset, itemPerPage);
    }


}
