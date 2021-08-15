package javapro.controller;

import javapro.api.request.LikeRequest;
import javapro.api.response.IsLikedResponse;
import javapro.api.response.LikeResponse;
import javapro.config.Config;
import javapro.config.exception.BadRequestException;
import javapro.config.exception.NotFoundException;
import javapro.services.LikesService;
import javapro.services.PostCommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static javapro.config.Config.LIKE_OBJECT_COMMENT;
import static javapro.config.Config.LIKE_OBJECT_POST;

@RestController
@Tag(name = "/api/v1/likes", description = "Работа с лайками")
public class LikesController {
    private final LikesService likesService;
    private final PostCommentService postCommentService;

    public LikesController(LikesService likesService,
                           PostCommentService postCommentService) {
        this.likesService = likesService;
        this.postCommentService = postCommentService;
    }

    //этот метод фронтом не вызывается
    @GetMapping(value = "/api/v1/liked")
    @Operation(description = "Ставим лайк на объект(коммент/пост)")
    @ApiResponse(responseCode = "200", description = "Лайк поставлен")
    @ApiResponse(responseCode = "400", description = "Тип объекта не задан")
    @ApiResponse(responseCode = "404", description = "id объекта не найден в базе")
    public ResponseEntity<IsLikedResponse> getLike(@PathVariable("user_id") Integer userID,
                                                   @PathVariable("item_id") Integer itemID,
                                                   @PathVariable String type) throws NotFoundException, BadRequestException {
        if (type.equals(LIKE_OBJECT_POST)) {
            return likesService.isLiked(itemID);
        }
        if (type.equals(LIKE_OBJECT_COMMENT)) {
            return postCommentService.isLiked(itemID);
        }
        throw new BadRequestException(Config.STRING_NO_CONTENT_TYPE);
    }

    //этот метод фронтом не вызывается
    @GetMapping(value = "/api/v1/likes")
    @Operation(description = "Получить лайки на объекте")
    @ApiResponse(responseCode = "200", description = "Лайки получены")
    @ApiResponse(responseCode = "400", description = "Тип объекта не задан")
    @ApiResponse(responseCode = "404", description = "id объекта не найден в базе")
    public ResponseEntity<LikeResponse> getLikes(@PathVariable("item_id") Integer itemID,
                                                 @PathVariable String type) throws BadRequestException {
        if (type.equals(LIKE_OBJECT_POST)) {
            return likesService.getAllLikes(itemID);
        }

        if (type.equals(LIKE_OBJECT_COMMENT)) {
            return postCommentService.getAllLikes(itemID);
        }

        throw new BadRequestException(Config.STRING_NO_CONTENT_TYPE);
    }

    @PutMapping(value = "/api/v1/likes")
    @Operation(description = "Поставить лайк на объект(коммент/пост)")
    @ApiResponse(responseCode = "200", description = "Лайки поставлены")
    @ApiResponse(responseCode = "400", description = "Тип объекта не задан")
    @ApiResponse(responseCode = "404", description = "id объекта не найден в базе")
    public ResponseEntity<LikeResponse> addLike(@RequestBody LikeRequest likeBody) throws NotFoundException, BadRequestException {

        if (likeBody.getType().equals(LIKE_OBJECT_POST)) {
            return likesService.addLike(likeBody.getItemID());
        }
        if (likeBody.getType().equals(LIKE_OBJECT_COMMENT)) {
            return postCommentService.addLike(likeBody.getItemID());
        }
        throw new BadRequestException(Config.STRING_NO_CONTENT_TYPE);
    }

    @DeleteMapping(value = "/api/v1/likes")
    @Operation(description = "Убрать лайк с объекта(коммент/пост)")
    @ApiResponse(responseCode = "200", description = "Лайк убран")
    @ApiResponse(responseCode = "400", description = "Тип объекта не задан")
    @ApiResponse(responseCode = "404", description = "id объекта не найден в базе")
    public ResponseEntity<LikeResponse> deleteLike(@RequestBody LikeRequest likeBody) throws NotFoundException, BadRequestException {
        if (likeBody.getType().equals(LIKE_OBJECT_POST)) {
            return likesService.deleteLike(likeBody.getItemID());
        }
        if (likeBody.getType().equals(LIKE_OBJECT_COMMENT)) {
            return postCommentService.deleteLike(likeBody.getItemID());
        }
        throw new BadRequestException(Config.STRING_NO_CONTENT_TYPE);
    }

}
