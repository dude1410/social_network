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
public class PlatformController {

    private final PlatformService platformService;

    public PlatformController(PlatformService platformService) {
        this.platformService = platformService;
    }

    @GetMapping(value = "/api/v1/platform/languages")
    @Operation(description = "Вывод всех доступный языков для выбора")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Язык для сайта выбран")})
    public ResponseEntity<PlatformResponse> languages(@RequestParam(value = "language",
            required = false) String language,
                                                      @RequestParam(value = "offset",
                                                              required = false,
                                                              defaultValue = "0") Integer offset,
                                                      @RequestParam(value = "itemPerPage",
                                                              required = false,
                                                              defaultValue = "20") Integer itemPerPage) {
        return platformService.getLanguages(language, offset, itemPerPage);

    }

    @GetMapping(value = "/api/v1/platform/countries")
    public ResponseEntity<PlatformResponse> countries(@RequestParam(value = "country", required = false) String country,
                                                      @RequestParam(value = "offset", required = false) Integer offset,
                                                      @RequestParam(value = "itemPerPage", required = false) Integer itemPerPage) {

        return platformService.getCountry(country, offset, itemPerPage);
    }

    @GetMapping(value = "/api/v1/platform/towns")
    public ResponseEntity<PlatformResponse> towns(@RequestParam(value = "countryId", required = true) Integer countryid,
                                                  @RequestParam(value = "town", required = false) Integer town,
                                                  @RequestParam(value = "offset", required = false) Integer offset,
                                                  @RequestParam(value = "itemPerPage", required = false) Integer itemPerPage) {

        return platformService.getTown(countryid, town, offset, itemPerPage);
    }

}
