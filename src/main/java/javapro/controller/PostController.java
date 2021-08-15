package javapro.controller;

import javapro.api.request.*;
import javapro.api.response.*;
import javapro.config.exception.AuthenticationException;
import javapro.config.exception.BadRequestException;
import javapro.config.exception.NotFoundException;
import javapro.services.PostCommentService;
import javapro.services.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@Tag(name = "/api/v1/post", description = "Операции с постами")
public class PostController {
    private final PostService postService;
    private final PostCommentService postCommentService;

    public PostController(PostService postService, PostCommentService postCommentService) {
        this.postService = postService;
        this.postCommentService = postCommentService;
    }

    @GetMapping(value = "/api/v1/feeds")
    @Operation(description = "Получить все посты")
    @ApiResponse(responseCode = "200", description = "Успешная попытка получить все посты")
    @ApiResponse(responseCode = "404", description = "Посты не найдены в БД")
    public ResponseEntity<PostResponse> getAllPosts(@RequestParam(value = "offset",
                                                            required = false,
                                                            defaultValue = "0") Integer offset,
                                                    @RequestParam(value = "itemPerPage",
                                                            required = false,
                                                            defaultValue = "20") Integer itemPerPage) throws NotFoundException {
        return postService.getAllPosts(offset, itemPerPage);
    }

    @PutMapping(value = "/api/v1/post/{id}")
    @Operation(description = "Редактирование поста")
    @ApiResponse(responseCode = "200", description = "Успешная попытка отредактировать пост по id")
    @ApiResponse(responseCode = "400", description = "id поста не задан")
    @ApiResponse(responseCode = "404", description = "Пост не найден в БД")
    public ResponseEntity<PostShortResponse> updatePostByID(@PathVariable Integer id,
                                                            @RequestBody PostDataRequest postDataRequest) throws NotFoundException,
            BadRequestException {

        return postService.updatePostByID(id, postDataRequest);
    }

    @DeleteMapping(value = "/api/v1/post/{id}")
    @Operation(description = "Удаление поста")
    @ApiResponse(responseCode = "200", description = "Успешная попытка удалить пост")
    @ApiResponse(responseCode = "401", description = "Пользователь не авторизован")
    @ApiResponse(responseCode = "400", description = "id поста не задан")
    @ApiResponse(responseCode = "404", description = "Пост не найден в БД")
    public ResponseEntity<DeletePostByIDResponse> deletePostByID(@PathVariable Integer id) throws AuthenticationException,
            NotFoundException,
            BadRequestException {
        return postService.deletePostByID(id);
    }

    @GetMapping(value = "/api/v1/post/{id}")
    @Operation(description = "Получения поста по id")
    @ApiResponse(responseCode = "200", description = "Успешная попытка получить пост")
    @ApiResponse(responseCode = "400", description = "id поста не задан")
    @ApiResponse(responseCode = "404", description = "Пост не найден в БД")
    public ResponseEntity<PostShortResponse> getPostByID(@PathVariable Integer id) throws NotFoundException,
            BadRequestException {
        return postService.getPostByID(id);
    }

    @PutMapping(value = "/api/v1/post/{id}/recover")
    @Operation(description = "Восстановление публикации по ID ")
    @ApiResponse(responseCode = "200", description = "Успешное восстановление публикации")
    @ApiResponse(responseCode = "400", description = "id поста не задан")
    @ApiResponse(responseCode = "404", description = "Пост не найден в БД")
    public ResponseEntity<PostShortResponse> recoverPost(@PathVariable Integer id) throws BadRequestException, NotFoundException {
        return postService.recoverPost(id);
    }

    //этот метод фронтом не вызывается
    @PostMapping(value = "/api/v1/post/{id}/report")
    @Operation(description = "Подать жалобу на публикацию")
    @ApiResponse(responseCode = "200", description = "Жалоба подана успешно")
    @ApiResponse(responseCode = "400", description = "id поста не задан")
    @ApiResponse(responseCode = "404", description = "Пост не найден в БД")
    public ResponseEntity<ReportCommentResponse> reportPost(@PathVariable Integer id) throws BadRequestException, NotFoundException {
        return postService.reportPost(id);
    }

    @PostMapping(value = "/api/v1/post/{id}/comments")
    @Operation(description = "Создание комментария")
    @ApiResponse(responseCode = "200", description = "Жалоба подана успешно")
    @ApiResponse(responseCode = "400", description = "id коммента не задан")
    @ApiResponse(responseCode = "404", description = "Коммент не найден в БД")
    public ResponseEntity<CommentResponse> addComment(@PathVariable Integer id,
                                                      @RequestBody CommentBodyRequest commentBody) throws NotFoundException, BadRequestException {
        return postCommentService.addComment(id, commentBody);
    }

    @GetMapping(value = "/api/v1/post/{id}/comments")
    @Operation(description = "Получить комментарии к посту")
    @ApiResponse(responseCode = "200", description = "Комментарии получены успешно")
    @ApiResponse(responseCode = "400", description = "id поста не задан")
    @ApiResponse(responseCode = "404", description = "Комменты не найдены в БД")
    public ResponseEntity<CommentsResponse> getCommentsByPostID(@PathVariable Integer id,
                                                                @RequestParam(value = "offset",
                                                                        required = false,
                                                                        defaultValue = "0") Integer offset,
                                                                @RequestParam(value = "itemPerPage",
                                                                        required = false,
                                                                        defaultValue = "20") Integer itemPerPage) throws BadRequestException {
        return postCommentService.getCommentsByPostID(id, offset, itemPerPage);
    }

    @PutMapping(value = "/api/v1/post/{id}/comments/{comment_id}")
    @Operation(description = "Редактирование комментария к публикации")
    @ApiResponse(responseCode = "200", description = "Редактирование комментария прошло успешно")
    @ApiResponse(responseCode = "400", description = "id коммента не задан")
    @ApiResponse(responseCode = "404", description = "Коммент не найден в БД")
    public ResponseEntity<CommentResponse> editComment(@PathVariable Integer id,
                                                       @PathVariable(name = "comment_id") Integer commentID,
                                                       @RequestBody CommentBodyRequest editComment) throws BadRequestException, NotFoundException {
        return postCommentService.editComment(commentID, editComment);
    }

    @DeleteMapping(value = "/api/v1/post/{id}/comments/{comment_id}")
    @Operation(description = "Удаление комментария к публикации")
    @ApiResponse(responseCode = "200", description = "Комментарий удален успешно")
    @ApiResponse(responseCode = "400", description = "id коммента не задан")
    @ApiResponse(responseCode = "404", description = "Коммент не найден в БД")
    public ResponseEntity<DeletePostByIDResponse> deleteComment(@PathVariable Integer id,
                                                                @PathVariable(name = "comment_id") Integer commentID) throws BadRequestException, NotFoundException {
        return postCommentService.deleteComment(commentID);
    }

    //этот метод фронтом не вызывается
    @PutMapping(value = "/api/v1/post/{id}/comments/{comment_id}/recover")
    @Operation(description = "Восстановление комментария к публикации")
    @ApiResponse(responseCode = "200", description = "Комментарий восстановлен успешно")
    @ApiResponse(responseCode = "400", description = "id коммента не задан")
    @ApiResponse(responseCode = "404", description = "Коммент не найден в БД")
    public ResponseEntity<CommentResponse> recoverComment(@PathVariable Integer id,
                                                          @PathVariable(name = "comment_id") Integer commentID) throws BadRequestException, NotFoundException {
        return postCommentService.recoverComment(commentID);
    }

    //этот метод фронтом не вызывается
    @PostMapping(value = "/api/v1/post/{id}/comments/{comment_id}/report")
    @Operation(description = "Жалоба на комментарий")
    @ApiResponse(responseCode = "200", description = "Жалоба на комментарий подана успешно")
    @ApiResponse(responseCode = "400", description = "id коммента не задан")
    @ApiResponse(responseCode = "404", description = "Коммент не найден в БД")
    public ResponseEntity<ReportCommentResponse> reportComment(@PathVariable Integer id,
                                                               @PathVariable(name = "comment_id") Integer commentID) throws BadRequestException, NotFoundException {
        return postCommentService.reportComment(commentID);
    }
}
