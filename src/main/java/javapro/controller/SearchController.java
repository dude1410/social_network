package javapro.controller;

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
    public ResponseEntity getPost(@RequestParam("text") String searchText,
                                  @RequestParam(value = "date_from", defaultValue = "") Long dateFrom,
                                  @RequestParam(value = "date_to", defaultValue = "") Long dateTo,
                                  @RequestParam(value = "author", defaultValue = "") String author) throws BadRequestException,
            UnAuthorizedException {
        return searchService.searchPosts(searchText, dateFrom, dateTo, author);
    }

    @GetMapping(value = "/api/v1/users/search")
    @Operation(description = "Поиск пользователей")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Успешная попытка найти пост по тексту"),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован"),
            @ApiResponse(responseCode = "400", description = "Не задан текст для посика")})
    public ResponseEntity getPeople(@RequestParam(value = "first_name", defaultValue = "") String firstName,
                                    @RequestParam(value = "last_name", defaultValue = "") String lastName,
                                    @RequestParam(value = "age_from", defaultValue = "") Integer ageFrom,
                                    @RequestParam(value = "age_to", defaultValue = "") Integer ageTo,
                                    @RequestParam(value = "country", defaultValue = "") String country,
                                    @RequestParam(value = "city", defaultValue = "") String town) throws BadRequestException, UnAuthorizedException {

        return searchService.searchPeopleByProperties(firstName, lastName, ageFrom, ageTo, country, town);
    }
}
