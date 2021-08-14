package javapro.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import javapro.api.response.PlatformResponse;
import javapro.model.Country;
import javapro.model.Town;
import javapro.model.dto.LanguageDTO;
import javapro.services.PlatformService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin
@RestController
public class PlatformController {

    private final PlatformService platformService;

    public PlatformController(PlatformService platformService) {
        this.platformService = platformService;
    }

    @GetMapping(value = "/api/v1/platform/languages")
    @Operation(description = "Вывод всех доступный языков для выбора")
    @ApiResponse(responseCode = "200", description = "Язык для сайта выбран")
    public ResponseEntity<PlatformResponse<List<LanguageDTO>>> languages(@RequestParam(value = "language",
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
    @Operation(description = "Выбор страны")
    @ApiResponse(responseCode = "200", description = "Страна выбрана")
    public ResponseEntity<PlatformResponse<Page<Country>>> countries(@RequestParam(value = "country", required = false) String country,
                                                                     @RequestParam(value = "offset", required = false) Integer offset,
                                                                     @RequestParam(value = "itemPerPage", required = false) Integer itemPerPage) {
        return platformService.getCountry(country, offset, itemPerPage);
    }

    @GetMapping(value = "/api/v1/platform/towns")
    @Operation(description = "Вывод всех доступный городов в стране")
    @ApiResponse(responseCode = "200", description = "Город в стране выбран")
    public ResponseEntity<PlatformResponse<Page<Town>>> towns(@RequestParam(value = "countryId") Integer countryid,
                                                              @RequestParam(value = "town", required = false) Integer town,
                                                              @RequestParam(value = "offset", required = false) Integer offset,
                                                              @RequestParam(value = "itemPerPage", required = false) Integer itemPerPage) {
        return platformService.getTown(countryid, town, offset, itemPerPage);
    }

}
