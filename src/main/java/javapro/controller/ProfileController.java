package javapro.controller;

import javapro.api.request.EditMyProfileRequest;
import javapro.api.request.PostDataRequest;
import javapro.api.response.*;
import javapro.config.Config;
import javapro.config.exception.AuthenticationException;
import javapro.config.exception.BadRequestException;
import javapro.config.exception.NotFoundException;
import javapro.services.PostService;
import javapro.services.ProfileService;
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

    @GetMapping(value = "/api/v1/users/{id}/wall")
    @Operation(description = "Получение записей на стене пользователя")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Успешная попытка открыть стену пользователя"),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован"),
            @ApiResponse(responseCode = "400", description = "id пользователя не задан")})
    public ResponseEntity<MyWallResponse> myWall(@PathVariable Integer id,
                                                 @RequestParam(name = "offset", defaultValue = "0") Integer offset,
                                                 @RequestParam(name = "itemPerPage", defaultValue = "20") Integer itemPerPage) throws NotFoundException {
        return postService.getPostsByUser(offset, itemPerPage);
    }

    @PostMapping(value = "/api/v1/users/{id}/wall")
    @Operation(description = "Создать пост на стене пользователя")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Успешная попытка создать пост"),
            @ApiResponse(responseCode = "400", description = "id пользователя не задан")})
    public ResponseEntity<PostShortResponse> publishPost(@PathVariable Integer id,
                                                         @RequestParam(name = "publish_date", required = false) Long publishDate,
                                                         @RequestBody PostDataRequest postDataRequest) throws NotFoundException {
        return postService.publishPost(publishDate,
                postDataRequest);
    }

    @PutMapping("/api/v1/users/me")
    @Operation(description = "Редактирование профиля")
    public ResponseEntity<Response> editMyProfile(@RequestBody EditMyProfileRequest editMyProfileRequest) throws AuthenticationException, NotFoundException, BadRequestException {
        return profileService.editMyProfile(editMyProfileRequest);
    }


    /**===================
     * by karachun_maskim
     * ==================*/
    @GetMapping("/api/v1/users/{id}")
    @Operation(description = "Просмотр страницы пользователя")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Успешное получение данных пользователя"),
            @ApiResponse(responseCode = "400", description = "Не удалось получить данные пользователя")})
    public ResponseEntity<ProfileByIdResponse> getUserById(@PathVariable Integer id, Principal principal) throws BadRequestException {
        if (principal == null) {
            throw new BadRequestException(Config.STRING_AUTH_ERROR);
        }
        return profileService.getProfileById(id);
    }
}
