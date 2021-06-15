package JavaPRO.controller;

import JavaPRO.api.request.PostUpdateRequest;
import JavaPRO.api.response.Response;
import JavaPRO.services.PostService;
import JavaPRO.services.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "/api/v1/users", description = "Операции с профилем")
public class ProfileController {
    private final ProfileService profileService;
    private final PostService postService;

    public ProfileController(ProfileService profileService, PostService postService) {
        this.profileService = profileService;
        this.postService = postService;
    }

    @GetMapping("/api/v1/users/me")
    @Operation(description = "Открыть профиль пользователя")
    public ResponseEntity<Response> me(){
        return profileService.getMyProfile();
    }

    @GetMapping("/api/v1/users/{id}/wall")
    @Operation(description = "Открыть страничку пользователя")
    public ResponseEntity<Response> myWall(@PathVariable int id){
        return postService.getPostsByUser(id);
    }

    @PostMapping("/api/v1/users/{id}/wall")
    @Operation(description = "Создать страничку пользователя")
    public ResponseEntity<Response> publishPost(@PathVariable int id,
                                                @RequestParam(name = "publish_date", required = false) Long publishDate,
                                                @RequestBody PostUpdateRequest postUpdateRequest) {
        return postService.publishPost(id, publishDate, postUpdateRequest);
    }
}
