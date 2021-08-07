package javapro.controller;

import javapro.api.response.PersonsResponse;
import javapro.api.response.PostResponse;
import javapro.config.exception.BadRequestException;
import javapro.config.exception.UnAuthorizedException;
import javapro.services.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SearchController {
    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping(value = "/api/v1/post")
    @Operation(description = "Поиск постов по тексту")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Успешная попытка найти пост по тексту"),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован"),
            @ApiResponse(responseCode = "400", description = "Не задан текст для поиска")})
    public ResponseEntity<PostResponse> getPost(@RequestParam("text") String searchText,
                                                @RequestParam(value = "date_from", defaultValue = "") Long dateFrom,
                                                @RequestParam(value = "date_to", defaultValue = "") Long dateTo,
                                                @RequestParam(value = "author", defaultValue = "") String author,
                                                @RequestParam(defaultValue = "0") Integer offset,
                                                @RequestParam(defaultValue = "20") Integer itemPerPage) throws BadRequestException,
            UnAuthorizedException {
        return searchService.searchPosts(searchText, dateFrom, dateTo, author, offset, itemPerPage);
    }

    @GetMapping(value = "/api/v1/users/search")
    @Operation(description = "Поиск пользователей")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Успешная попытка найти пользователя по тексту"),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован"),
            @ApiResponse(responseCode = "400", description = "Не задан текст для поиска")})
    public ResponseEntity<PersonsResponse> getPeople(@RequestParam(value = "first_name", defaultValue = "") String firstName,
                                                     @RequestParam(value = "last_name", defaultValue = "") String lastName,
                                                     @RequestParam(value = "age_from", defaultValue = "") Integer ageFrom,
                                                     @RequestParam(value = "age_to", defaultValue = "") Integer ageTo,
                                                     @RequestParam(value = "country", defaultValue = "") String country,
                                                     @RequestParam(value = "city", defaultValue = "") String town,
                                                     @RequestParam(defaultValue = "0") Integer offset,
                                                     @RequestParam(defaultValue = "20") Integer itemPerPage) throws UnAuthorizedException {

        return searchService.searchPeopleByProperties(firstName, lastName, ageFrom, ageTo, country, town, offset, itemPerPage);
    }

    //searchBarPeople
    @GetMapping(value = "/api/v1/users/searchbar")
    @Operation(description = "Основная строка | Поиск пользователей")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Успешная попытка найти пользователя по тексту"),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован"),
            @ApiResponse(responseCode = "400", description = "Не задан текст для поиска")})
    public ResponseEntity<PersonsResponse> getPeopleGeneral(@RequestParam(value = "searchtext", defaultValue = "") String searchText,
                                                            @RequestParam(defaultValue = "0", required = false) Integer offset,
                                                            @RequestParam(defaultValue = "20", required = false) Integer itemPerPage) throws BadRequestException,
            UnAuthorizedException {

        return searchService.searchPeopleGeneral(searchText, offset, itemPerPage);
    }

    //searchBarPosts
    @GetMapping(value = "/api/v1/posts/searchbar")
    @Operation(description = "Основная строка | Поиск постов")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Успешная попытка найти пост по тексту"),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован"),
            @ApiResponse(responseCode = "400", description = "Не задан текст для поиска")})
    public ResponseEntity<PostResponse> getPostsGeneral(@RequestParam(value = "searchtext", defaultValue = "") String searchText,
                                                        @RequestParam(defaultValue = "0", required = false) Integer offset,
                                                        @RequestParam(defaultValue = "20", required = false) Integer itemPerPage) throws BadRequestException,
            UnAuthorizedException {

        return searchService.searchPostsGeneral(searchText, offset, itemPerPage);
    }
}
