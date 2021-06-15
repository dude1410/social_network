package JavaPRO.controller;

import JavaPRO.api.request.PostUpdateRequest;
import JavaPRO.services.PostService;
import io.swagger.v3.oas.annotations.Operation;
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
    public ResponseEntity getPost(@RequestParam("text") String searchText) {
        return postService.searchPostsByText(searchText);
    }

    @GetMapping(value = "/api/v1/feeds")
    @Operation(description = "Получить все посты")
    public ResponseEntity getAllPosts(){
        return postService.getAllPosts();
    }

    @PutMapping("/api/v1/post/{id}")
    @Operation(description = "Редактирование поста")
    public ResponseEntity updatePostByID(@PathVariable int id,
                                         @RequestBody PostUpdateRequest postUpdateRequest){
        return postService.updatePostByID(id, postUpdateRequest.getTitle(), postUpdateRequest.getPost_text());
    }

    @DeleteMapping("/api/v1/post/{id}")
    @Operation(description = "Удаление поста")
    public ResponseEntity deletePostByID(@PathVariable int id) {
        return postService.deletePostByID(id);
    }

    @GetMapping("/api/v1/post/{id}")
    @Operation(description = "Поиск поста по id")
    public ResponseEntity getPostByID(@PathVariable int id) {
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
