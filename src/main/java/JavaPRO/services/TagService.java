package JavaPRO.services;

import JavaPRO.Util.TagToDTOMapper;
import JavaPRO.api.request.TagRequest;
import JavaPRO.api.response.TagDeleteResponse;
import JavaPRO.api.response.TagResponse;
import JavaPRO.api.response.TagsResponse;
import JavaPRO.config.Config;
import JavaPRO.config.exception.BadRequestException;
import JavaPRO.config.exception.NotFoundException;
import JavaPRO.model.DTO.TagDTO;
import JavaPRO.model.DTO.TagDeleteDTO;
import JavaPRO.model.Tag;
import JavaPRO.repository.TagRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Slf4j
@Service
public class TagService {

    private final TagRepository tagRepository;
    private final TagToDTOMapper tagToDTOMapper;

    public TagService(TagRepository tagRepository,
                      TagToDTOMapper tagToDTOMapper) {
        this.tagRepository = tagRepository;
        this.tagToDTOMapper = tagToDTOMapper;
    }

    public ResponseEntity<TagResponse> addTag(TagRequest tagRequest) throws BadRequestException {

        String tagName = tagRequest.getTag();

        if (tagName.isEmpty() || tagName.isBlank()){
            throw new BadRequestException(Config.STRING_NO_TAG_NAME);
        }

        if (tagRepository.findTagByName(tagName) != null) {
            throw new BadRequestException(Config.STRING_TAG_EXISTS_IN_DB);
        }

        Tag newTag = new Tag();
        newTag.setTag(tagName);

        tagRepository.save(newTag);

        TagDTO tagDTO = tagToDTOMapper.convertToDto(newTag);

        return ResponseEntity
                .ok(new TagResponse("successfully",
                        new Timestamp(System.currentTimeMillis()).getTime(),
                        tagDTO
                ));
    }

    public ResponseEntity<TagsResponse> getTags(String tagText) throws BadRequestException {

        if (tagText.isEmpty() || tagText.isBlank()){
            throw new BadRequestException(Config.STRING_NO_TAG_NAME);
        }

        List<Tag> tagsList = tagRepository.findTagsByText(tagText.toLowerCase(Locale.ROOT));

        List<TagDTO> tagDTOs = new ArrayList<>();

        tagsList.forEach(tag -> tagDTOs.add(tagToDTOMapper.convertToDto(tag)));

        return ResponseEntity
                .ok(new TagsResponse("successfully",
                        new Timestamp(System.currentTimeMillis()).getTime(),
                        0,
                        0,
                        20,
                        tagDTOs
                ));
    }

    public ResponseEntity<TagDeleteResponse> deleteTag(Integer tagID) throws BadRequestException, NotFoundException {

        if (tagID == null) {
            throw new BadRequestException(Config.STRING_NO_TAG_ID);
        }

        Tag tag = tagRepository.findTagByID(tagID);

        if (tag == null) {
            throw new NotFoundException(Config.STRING_NO_TAG_IN_DB);
        }

        tagRepository.deleteById(tagID);

        return ResponseEntity
                .ok(new TagDeleteResponse("successfully",
                        new Timestamp(System.currentTimeMillis()).getTime(),
                        new TagDeleteDTO()
                ));
    }

}
