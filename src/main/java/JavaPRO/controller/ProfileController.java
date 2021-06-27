package JavaPRO.controller;

import JavaPRO.api.request.PostUpdateRequest;
import JavaPRO.api.response.LoginResponse;
import JavaPRO.api.response.MyWallResponse;
import JavaPRO.api.response.PostShortResponse;
import JavaPRO.api.response.ProfileByIdResponse;
import JavaPRO.config.Config;
import JavaPRO.config.exception.AuthenticationException;
import JavaPRO.config.exception.BadRequestException;
import JavaPRO.config.exception.NotFoundException;
import JavaPRO.services.PostService;
import JavaPRO.services.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@Tag(name = "/api/v1/users", description = "Операции с профилем")
public class ProfileController {
    private final ProfileService profileService;
    private final PostService postService;

    public ProfileController(ProfileService profileService,
                             PostService postService) {
        this.profileService = profileService;
        this.postService = postService;
    }

    @GetMapping("/api/v1/users/me")
    @Operation(description = "Открыть профиль пользователя")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Профиль пользователя открыт"),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден в БД")})
    public ResponseEntity<LoginResponse> me() throws AuthenticationException,
            NotFoundException {
        return profileService.getMyProfile();
    }

    @GetMapping("/api/v1/users/{id}/wall")
    @Operation(description = "Открыть страничку пользователя")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Успешная попытка открытьстраничку пользователя"),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован"),
            @ApiResponse(responseCode = "400", description = "id пользователя не задан"),
            @ApiResponse(responseCode = "404", description = "Посты не найдены в БД")})
    public ResponseEntity<MyWallResponse> myWall(@PathVariable Integer id) throws NotFoundException {
        return postService.getPostsByUser();
    }

    @PostMapping("/api/v1/users/{id}/wall")
    @Operation(description = "Создать пост на страничке пользователя") // todo
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Успешная попытка создать страничку пользователя"), // todo
            @ApiResponse(responseCode = "400", description = "id пользователя не задан")})
    public ResponseEntity<PostShortResponse> publishPost(@PathVariable Integer id,
                                                         @RequestParam(name = "publish_date", required = false) Long publishDate,
                                                         @RequestBody PostUpdateRequest postUpdateRequest) throws BadRequestException, NotFoundException {
        return postService.publishPost(publishDate,
                postUpdateRequest);
    }

    /**===================
     * by karachun_maskim
     * ==================*/
    @GetMapping("/api/v1/users/{id}")
    @Operation(description = "Создать пост на страничке пользователя")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Успешное получение данных пользователя"),
            @ApiResponse(responseCode = "400", description = "Не удалось получить данные пользователя")})
    public ResponseEntity<ProfileByIdResponse> getUserById(@PathVariable Integer id, Principal principal) throws BadRequestException {
        if (principal == null) {
            throw new BadRequestException(Config.STRING_AUTH_ERROR);
        }
        return profileService.getProfileById(id);
    }
}
