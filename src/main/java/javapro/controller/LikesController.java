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
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    //TODO этот метод фронтом не вызывается
    @GetMapping(value = "/api/v1/liked")
    @Operation(description = "Ставим лайк на объект(коммент/пост)")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Лайк поставлен"),
            @ApiResponse(responseCode = "400", description = "Тип объекта не задан"),
            @ApiResponse(responseCode = "404", description = "id объекта не найден в базе")})
    public ResponseEntity<IsLikedResponse> getLike(@PathVariable Integer user_id,
                                                   @PathVariable("item_id") Integer itemID,
                                                   @PathVariable String type) throws NotFoundException, BadRequestException {
        if (type.equals("Post")) {
            return likesService.isLiked(itemID);
        }
        if (type.equals("Comment")) {
            return postCommentService.isLiked(itemID);
        }
        throw new BadRequestException(Config.STRING_NO_CONTENT_TYPE);
    }

    //TODO этот метод фронтом не вызывается
    @GetMapping(value = "/api/v1/likes")
    @Operation(description = "Получить лайки на объекте")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Лайки получены"),
            @ApiResponse(responseCode = "400", description = "Тип объекта не задан"),
            @ApiResponse(responseCode = "404", description = "id объекта не найден в базе")})
    public ResponseEntity<LikeResponse> getLikes(@PathVariable("item_id") Integer itemID,
                                                 @PathVariable String type) throws BadRequestException {
        if (type.equals("Post")) {
            return likesService.getAllLikes(itemID);
        }

        if (type.equals("Comment")) {
            return postCommentService.getAllLikes(itemID);
        }

        throw new BadRequestException(Config.STRING_NO_CONTENT_TYPE);
    }

    @PutMapping(value = "/api/v1/likes")
    @Operation(description = "Поставить лайк на объект(коммент/пост)")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Лайки поставлены"),
            @ApiResponse(responseCode = "400", description = "Тип объекта не задан"),
            @ApiResponse(responseCode = "404", description = "id объекта не найден в базе")})
    public ResponseEntity<LikeResponse> addLike(@RequestBody LikeRequest likeBody) throws NotFoundException, BadRequestException {

        if (likeBody.getType().equals("Post")) {
            return likesService.addLike(likeBody.getItem_id());
        }
        if (likeBody.getType().equals("Comment")) {
            return postCommentService.addLike(likeBody.getItem_id());
        }
        throw new BadRequestException(Config.STRING_NO_CONTENT_TYPE);
    }

    @DeleteMapping(value = "/api/v1/likes")
    @Operation(description = "Убрать лайк с объекта(коммент/пост)")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Лайк убран"),
            @ApiResponse(responseCode = "400", description = "Тип объекта не задан"),
            @ApiResponse(responseCode = "404", description = "id объекта не найден в базе")})
    public ResponseEntity<LikeResponse> deleteLike(@RequestBody LikeRequest likeBody) throws NotFoundException, BadRequestException {
        if (likeBody.getType().equals("Post")) {
            return likesService.deleteLike(likeBody.getItem_id());
        }
        if (likeBody.getType().equals("Comment")) {
            return postCommentService.deleteLike(likeBody.getItem_id());
        }
        throw new BadRequestException(Config.STRING_NO_CONTENT_TYPE);
    }

}
