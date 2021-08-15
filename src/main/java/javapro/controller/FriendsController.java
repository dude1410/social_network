package javapro.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import javapro.api.request.IsFriendRequest;
import javapro.api.response.FriendsResponse;
import javapro.api.response.IsFriendResponse;
import javapro.api.response.OkResponse;
import javapro.config.exception.AuthenticationException;
import javapro.config.exception.BadRequestException;
import javapro.config.exception.NotFoundException;
import javapro.services.FriendsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "/api/v1/friends", description = "Работа с друзьями")
public class FriendsController {

    private final FriendsService friendsService;

    @Autowired
    public FriendsController(FriendsService friendsService) {
        this.friendsService = friendsService;
    }

    @GetMapping(value = "/friends",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Поиск друзей")
    @ApiResponse(responseCode = "200", description = "Успешная попытка найти друзей")
    @ApiResponse(responseCode = "404", description = "Список друзей пуст")
    @ApiResponse(responseCode = "401", description = "Пользователь не авторизован")
    public ResponseEntity<FriendsResponse> getFriends(@RequestParam(value = "name", required = false) String name,
                                                      @RequestParam(value = "offset", defaultValue = "0", required = false) Long offset,
                                                      @RequestParam(value = "itemPerPage", defaultValue = "20") Long itemPerPage)
            throws AuthenticationException, NotFoundException {
        return friendsService.getFriends(name, offset, itemPerPage);
    }

    @DeleteMapping(value = "/friends/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Удаление из друзей")
    @ApiResponse(responseCode = "200", description = "Успешная попытка удалить из друзей")
    @ApiResponse(responseCode = "400", description = "Некорректный запрос")
    @ApiResponse(responseCode = "401", description = "Пользователь не авторизован")
    @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    public ResponseEntity<OkResponse> deleteFriend(@PathVariable Integer id)
            throws AuthenticationException, NotFoundException, BadRequestException {
        return friendsService.deleteFriend(id);
    }

    @PostMapping(value = "/friends/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Отправить заявку на добавление в друзья")
    @ApiResponse(responseCode = "200", description = "Заявка на добавление в друзья направлена")
    @ApiResponse(responseCode = "400", description = "Некорректный запрос")
    @ApiResponse(responseCode = "401", description = "Пользователь не авторизован")
    @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    public ResponseEntity<OkResponse> sendRequest(@PathVariable Integer id)
            throws AuthenticationException, BadRequestException, NotFoundException {
        return friendsService.sendRequest(id);
    }

    @GetMapping(value = "/friends/request",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Получить список заявок в друзья")
    @ApiResponse(responseCode = "200", description = "Успешная попытка получить список друзей")
    @ApiResponse(responseCode = "401", description = "Пользователь не авторизован")
    @ApiResponse(responseCode = "404", description = "Пользователь не найден / пользователь уже у вас в друзьях")
    public ResponseEntity<FriendsResponse> getRequestList(@RequestParam(value = "name", required = false) String name,
                                                          @RequestParam(value = "offset", defaultValue = "0", required = false) Long offset,
                                                          @RequestParam(value = "itemPerPage", defaultValue = "20") Long itemPerPage)
            throws AuthenticationException, NotFoundException {
        return friendsService.getRequestList(name, offset, itemPerPage);
    }

    @PostMapping(value = "/is/friends",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Получить информацию, является ли пользователь другом")
    @ApiResponse(responseCode = "200", description = "Успешная попытка получить список друзей")
    @ApiResponse(responseCode = "400", description = "Некорректный ввод данных")
    @ApiResponse(responseCode = "401", description = "Пользователь не авторизован")
    @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    public ResponseEntity<IsFriendResponse> checkFriendStatus(@RequestBody IsFriendRequest request)
            throws AuthenticationException, NotFoundException, BadRequestException {
        return friendsService.checkFriendStatus(request);
    }

    @GetMapping(value = "/friends/recommendations")
    @Operation(description = "Получить список рекомендаций")
    @ApiResponse(responseCode = "200", description = "Успешная попытка получить список рекомендаций")
    @ApiResponse(responseCode = "401", description = "Пользователь не авторизован")
    @ApiResponse(responseCode = "400", description = "Список рекомендаций не удалось составить")
    @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    public ResponseEntity<FriendsResponse> getRecommendations(@RequestParam(value = "offset", required = false) Long offset,

                                                              @RequestParam(value = "itemPerPage", required = false) Long itemPerPage)
            throws AuthenticationException, NotFoundException {
        return friendsService.getRecommendations(offset, itemPerPage);
    }

    @PutMapping(value = "/friends/{id}")
    @Operation(description = "Принять/добавить в друзья")
    @ApiResponse(responseCode = "200", description = "Успешная попытка добавить в друзья")
    @ApiResponse(responseCode = "400", description = "Некорректный запрос")
    @ApiResponse(responseCode = "401", description = "Пользователь не авторизован")
    @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    public ResponseEntity<OkResponse> addFriend(@PathVariable Integer id)
            throws AuthenticationException, NotFoundException, BadRequestException {
        return friendsService.addFriend(id);
    }
}



