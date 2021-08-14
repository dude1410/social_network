package javapro.services;

import javapro.api.response.PlatformResponse;
import javapro.model.Country;
import javapro.model.Town;
import javapro.model.dto.LanguageDTO;
import javapro.model.enums.Language;
import javapro.repository.CountryRepository;
import javapro.repository.TownRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class PlatformService {

    private final CountryRepository countryRepository;
    private final TownRepository townRepository;


    public PlatformService(CountryRepository countryRepository,
                           TownRepository townRepository) {
        this.countryRepository = countryRepository;
        this.townRepository = townRepository;
    }


    public ResponseEntity<PlatformResponse<List<LanguageDTO>>> getLanguages(String language, Integer offset, Integer itemPerPage) {

        List<LanguageDTO> data = new ArrayList<>();

        data.add(new LanguageDTO(1, Language.Русский.toString()));
        data.add(new LanguageDTO(2, Language.English.toString()));

        return ResponseEntity.ok(new PlatformResponse("ok",
                new Timestamp(System.currentTimeMillis()).getTime(),
                data.size(),
                offset,
                itemPerPage,
                data));
    }

    public ResponseEntity<PlatformResponse<Page<Country>>> getCountry(String country, Integer offset, Integer itemPerPage) {
        itemPerPage = (itemPerPage == null) ? 20 : itemPerPage;
        var platformResponse = new PlatformResponse<Page<Country>>();

        var pageable = PageRequest.of((offset == null) ? 0 : offset / itemPerPage, itemPerPage, Sort.by("name"));
        Page<Country> countries = (country == null) ?
                countryRepository.findAll(pageable) :
                countryRepository.findOne(pageable, country);
        platformResponse.setTotal(Math.toIntExact(countries.getTotalElements()));
        platformResponse.setData(countries);
        platformResponse.setError("ok");
        platformResponse.setOffset((offset == null) ? 0 : offset);
        platformResponse.setPerPage(itemPerPage);
        platformResponse.setTimestamp(new Timestamp(System.currentTimeMillis()).getTime());
        return ResponseEntity.ok(platformResponse);
    }

    public ResponseEntity<PlatformResponse<Page<Town>>> getTown(Integer countryId, Integer town, Integer offset, Integer itemPerPage) {
        var platformResponse = new PlatformResponse<Page<Town>>();
        itemPerPage = (itemPerPage == null) ? 20 : itemPerPage;
        var pageable = PageRequest.of((offset == null) ? 0 : offset / itemPerPage, itemPerPage, Sort.by("name"));
        Page<Town> towns;

        if (town == null) {
            towns = townRepository.findAll(pageable, countryId);
        } else {
            towns = townRepository.findOne(pageable, countryId, town);
        }


        platformResponse.setError("ok");
        platformResponse.setTotal(Math.toIntExact(towns.getTotalElements()));
        platformResponse.setTimestamp(new Timestamp(System.currentTimeMillis()).getTime());
        platformResponse.setOffset((offset == null) ? 0 : offset);
        platformResponse.setData(towns);
        platformResponse.setPerPage(itemPerPage);
        return ResponseEntity.ok(platformResponse);
    }
}
