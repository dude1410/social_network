package javapro.services;

import javapro.util.TagToDTOMapper;
import javapro.api.request.TagRequest;
import javapro.api.response.TagDeleteResponse;
import javapro.api.response.TagResponse;
import javapro.api.response.TagsResponse;
import javapro.config.Config;
import javapro.config.exception.BadRequestException;
import javapro.config.exception.NotFoundException;
import javapro.model.dto.TagDTO;
import javapro.model.dto.TagDeleteDTO;
import javapro.model.Tag;
import javapro.repository.TagRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

        TagDTO tagDTO = tagToDTOMapper.convertToDTO(newTag);

        return ResponseEntity
                .ok(new TagResponse(Config.WALL_RESPONSE,
                        new Timestamp(System.currentTimeMillis()).getTime(),
                        tagDTO
                ));
    }

    public ResponseEntity<TagsResponse> getTags(String tagText,
                                                Integer offset,
                                                Integer itemPerPage) {

        Pageable pageable = PageRequest.of(offset / itemPerPage, itemPerPage);
        Page<Tag> tagsList = tagRepository.findTagsByText(tagText.toLowerCase(Locale.ROOT), pageable);

        List<TagDTO> tagDTOs = new ArrayList<>();

        tagsList.forEach(tag -> tagDTOs.add(tagToDTOMapper.convertToDTO(tag)));

        return ResponseEntity
                .ok(new TagsResponse(Config.WALL_RESPONSE,
                        new Timestamp(System.currentTimeMillis()).getTime(),
                        (int) tagsList.getTotalElements(),
                        offset,
                        itemPerPage,
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
                .ok(new TagDeleteResponse(Config.WALL_RESPONSE,
                        new Timestamp(System.currentTimeMillis()).getTime(),
                        new TagDeleteDTO()
                ));
    }

}
