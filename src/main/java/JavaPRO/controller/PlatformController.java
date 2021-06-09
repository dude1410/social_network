package JavaPRO.controller;

import JavaPRO.api.response.Response;
import JavaPRO.services.PlatformServise;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PlatformController {

    private final PlatformServise platformServise;

    public PlatformController(PlatformServise platformServise) {
        this.platformServise = platformServise;
    }

    @GetMapping(value = "/api/v1/platform/languages")
    public ResponseEntity<Response> languages(@RequestParam(value = "language", required = false) String language,
                                              @RequestParam(value = "offset", required = false) Integer offset,
                                              @RequestParam(value = "itemPerPage", required = false) Integer itemPerPage) {
        itemPerPage = 20;
        return platformServise.getLanguages();

    }
}
