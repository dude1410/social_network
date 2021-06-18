package JavaPRO.services;

import JavaPRO.api.response.PlatformResponse;
import JavaPRO.model.DTO.LanguageDTO;
import JavaPRO.model.ENUM.Language;
import JavaPRO.repository.CountryRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class PlatformService {

    private final CountryRepository countryRepository;

    public PlatformService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }


    public ResponseEntity<PlatformResponse> getLanguages() {

//todo заглушка на 1 язык
        return ResponseEntity.ok(new PlatformResponse("ok",
                new Timestamp(System.currentTimeMillis()).getTime(),
                1,
                0,
                20,
                new LanguageDTO(1, Language.Русский)));
    }
}
