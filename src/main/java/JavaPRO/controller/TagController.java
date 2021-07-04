package JavaPRO.controller;

import JavaPRO.api.request.TagRequest;
import JavaPRO.api.response.TagDeleteResponse;
import JavaPRO.api.response.TagResponse;
import JavaPRO.api.response.TagsResponse;
import JavaPRO.config.exception.BadRequestException;
import JavaPRO.config.exception.NotFoundException;
import JavaPRO.services.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "/api/v1/tags/", description = "Работа с тегами")
public class TagController {

    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @PostMapping("/api/v1/tags/")
    @Operation(description = "Добавить тег")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Тег успешно создан"),
            @ApiResponse(responseCode = "400", description = "Название тега не задано или задано некорректно")})
    public ResponseEntity<TagResponse> addTag(@RequestBody TagRequest tagRequest) throws BadRequestException {
        return tagService.addTag(tagRequest);
    }

    @GetMapping("/api/v1/tags/")
    @Operation(description = "Получение тегов для публикации")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Тег успешно удален"),
            @ApiResponse(responseCode = "400", description = "Не передан тэг")})
    public ResponseEntity<TagsResponse> getTags(@RequestParam String tag,
                                                @RequestParam(defaultValue = "0l") Long offset,
                                                @RequestParam(defaultValue = "20l") Long itemPerPage) throws BadRequestException {
        return tagService.getTags(tag);
    }

    @DeleteMapping("/api/v1/tags/")
    @Operation(description = "Удалить тег")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Тег успешно удален"),
            @ApiResponse(responseCode = "400", description = "Не передан id тега"),
            @ApiResponse(responseCode = "404", description = "Тег не найден в базе")})
    public ResponseEntity<TagDeleteResponse> deleteTag(@RequestParam Integer id) throws BadRequestException, NotFoundException {
        return tagService.deleteTag(id);
    }

}
