package JavaPRO.controller;

import JavaPRO.api.response.Response;
import JavaPRO.services.ProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProfileController {
    private final ProfileService profileService;


    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping("/api/v1/users/me")
    public ResponseEntity<Response> me(){
        return profileService.getMyProfile();
    }
}
