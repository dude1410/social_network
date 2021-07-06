package javapro.services;

import javapro.api.response.PlatformResponse;
import javapro.model.Country;
import javapro.model.dto.LanguageDTO;
import javapro.model.enums.Language;
import javapro.model.Town;
import javapro.repository.CountryRepository;
import javapro.repository.TownRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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


    public ResponseEntity<PlatformResponse> getLanguages(String language, Integer offset, Integer itemPerPage) {

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

    public ResponseEntity<PlatformResponse> getCountry(String country, Integer offset, Integer itemPerPage) {
        itemPerPage = (itemPerPage == null) ? 20 : itemPerPage;
        PlatformResponse platformResponse = new PlatformResponse();

        Page<Country> countries;

        if (country == null) {
            countries = countryRepository.findAll(PageRequest.of(offset / itemPerPage, itemPerPage));
        } else {
            countries = countryRepository.findOne(PageRequest.of(offset / itemPerPage, itemPerPage), country);
        }

        platformResponse.setTotal(Math.toIntExact(countries.getTotalElements()));
        platformResponse.setData(countries);
        platformResponse.setError("ok");
        platformResponse.setOffset((offset == null) ? 0 : offset);
        platformResponse.setPerPage(itemPerPage);
        platformResponse.setTimestamp(new Timestamp(System.currentTimeMillis()).getTime());
        return ResponseEntity.ok(platformResponse);
    }

    public ResponseEntity<PlatformResponse> getTown(Integer countryId, Integer town, Integer offset, Integer itemPerPage) {
        PlatformResponse platformResponse = new PlatformResponse();
        itemPerPage = (itemPerPage == null) ? 20 : itemPerPage;
        Page<Town> towns;

        if (town == null) {
            towns = townRepository.findAll(PageRequest.of(offset / itemPerPage, itemPerPage), countryId);
        } else {
            towns = townRepository.findOne(PageRequest.of(offset / itemPerPage, itemPerPage), countryId, town);
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
