package JavaPRO.controller;

import JavaPRO.api.response.PlatformResponse;
import JavaPRO.services.PlatformService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "/api/v1/platform/languages", description = "Выбор языка интерфейса")
public class PlatformController {

    private final PlatformService platformService;

    public PlatformController(PlatformService platformService) {
        this.platformService = platformService;
    }

    @GetMapping(value = "/api/v1/platform/languages")
    @Operation(description = "Вывод всех доступный языков для выбора")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Язык для сайта выбран")})
    public ResponseEntity<PlatformResponse> languages(@RequestParam(value = "language", required = false) String language,
                                                      @RequestParam(value = "offset", required = false) Integer offset,
                                                      @RequestParam(value = "itemPerPage", required = false) Integer itemPerPage) {
        itemPerPage = 20;
        return platformService.getLanguages();

    }
}
