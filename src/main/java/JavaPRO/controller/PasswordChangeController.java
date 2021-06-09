package JavaPRO.controller;

import JavaPRO.api.request.OnlyPasswordRequest;
import JavaPRO.api.response.Response;
import JavaPRO.services.PasswordChangeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.Principal;

public class PasswordChangeController {

    private final PasswordChangeService passwordChangeService;

    public PasswordChangeController(PasswordChangeService passwordChangeService) {
        this.passwordChangeService = passwordChangeService;
    }

    @PutMapping(value = "/api/v1/account/password", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Response> passwordRecovery(@RequestBody OnlyPasswordRequest onlyPasswordRequest) {
        String currentEmail = Principal.class.getName();
        return passwordChangeService.changePassword(onlyPasswordRequest.getPassword(), currentEmail);
    }
}
