package JavaPRO.services;

import JavaPRO.api.response.PlatformResponse;
import JavaPRO.model.DTO.LanguageDTO;
import JavaPRO.model.ENUM.Language;
import JavaPRO.repository.CountryRepository;
import com.vdurmont.emoji.Emoji;
import com.vdurmont.emoji.EmojiManager;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class PlatformService {

    private final CountryRepository countryRepository;

    public PlatformService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
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
}
