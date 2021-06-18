package JavaPRO.controller;

import JavaPRO.api.request.PostUpdateRequest;
import JavaPRO.config.exception.AuthenticationException;
import JavaPRO.config.exception.BadRequestException;
import JavaPRO.config.exception.NotFoundException;
import JavaPRO.services.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "/api/v1/post", description = "Операции с постами")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping(value = "/api/v1/post")
    @Operation(description = "Поиск постов по тексту")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Успешная попытка найти пост по тексту"),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован"),
            @ApiResponse(responseCode = "400", description = "Не задан текст для посика")})
    public ResponseEntity getPost(@RequestParam("text") String searchText) throws BadRequestException,
            AuthenticationException {
        return postService.searchPostsByText(searchText);
    }

    @GetMapping(value = "/api/v1/feeds")
    @Operation(description = "Получить все посты")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Успешная попытка получить все посты"),
            @ApiResponse(responseCode = "404", description = "Посты не найдены в БД")})
    public ResponseEntity getAllPosts() throws NotFoundException {
        return postService.getAllPosts();
    }

    @PutMapping("/api/v1/post/{id}")
    @Operation(description = "Редактирование поста")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Успешная попытка отредактировать пост по id"),
            @ApiResponse(responseCode = "400", description = "id поста не задан"),
            @ApiResponse(responseCode = "404", description = "Пост не найден в БД")})
    public ResponseEntity updatePostByID(@PathVariable Integer id,
                                         @RequestBody PostUpdateRequest postUpdateRequest) throws NotFoundException,
            BadRequestException {
        return postService.updatePostByID(id,
                postUpdateRequest.getTitle(),
                postUpdateRequest.getPost_text());
    }

    @DeleteMapping("/api/v1/post/{id}")
    @Operation(description = "Удаление поста")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Успешная попытка удалить пост"),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован"),
            @ApiResponse(responseCode = "400", description = "id поста не задан"),
            @ApiResponse(responseCode = "404", description = "Пост не найден в БД")})
    public ResponseEntity deletePostByID(@PathVariable Integer id) throws AuthenticationException,
            NotFoundException,
            BadRequestException {
        return postService.deletePostByID(id);
    }

    @GetMapping("/api/v1/post/{id}")
    @Operation(description = "Получения поста по id")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Успешная попытка получить пост"),
            @ApiResponse(responseCode = "400", description = "id поста не задан"),
            @ApiResponse(responseCode = "404", description = "Пост не найден в БД")})
    public ResponseEntity getPostByID(@PathVariable Integer id) throws NotFoundException,
            BadRequestException {
        return postService.getPostByID(id);
    }



/*
    @PutMapping("/api/v1/post/{id}")
    public ResponseEntity editPostText(@PathVariable int id, Post newPostText) {
        Post post = postRepository.findById(id).get();

        if (post != null) {
            post.setPostText(newPostText.getPostText());
            postRepository.save(post);
            return new ResponseEntity(HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/api/v1/post/{id}")
    public ResponseEntity deletePostByID(@PathVariable int id) {

        Optional<Post> post = postRepository.findById(id);

        if (post.isPresent()) {
            postRepository.deleteById(id);
            return new ResponseEntity(HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    //PUT /api/v1/post/{id}/recover

    @GetMapping("/api/v1/post/{id}/comments")
    public ResponseEntity getCommentsByPostID(@PathVariable int id) {

        List<PostComment> postCommentsList = postRepository.findPostComments(id);

        if (!postCommentsList.isEmpty()) {
            return new ResponseEntity(postCommentsList, HttpStatus.OK);
        }
        return new ResponseEntity(postCommentsList, HttpStatus.OK);
    }

    //POST /api/v1/post/{id}/comments

    //PUT /api/v1/post/{id}/comments/{comment_id}

    //DELETE /api/v1/post/{id}/comments/{comment_id}

    //PUT /api/v1/post/{id}/comments/{comment_id}/recover

    //POST /api/v1/post/{id}/report

    //POST /api/v1/post/{id}/comments/{comment_id}/report

 */
}
