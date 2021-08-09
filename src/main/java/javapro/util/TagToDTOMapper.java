package javapro.util;

import javapro.model.dto.TagDTO;
import javapro.model.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TagToDTOMapper {

    private final ModelMapper modelMapper;

    public TagToDTOMapper() {
        this.modelMapper = new ModelMapper();
        modelMapper.createTypeMap(Tag.class, TagDTO.class);
    }

    public TagDTO convertToDTO(Tag tag) {
        return modelMapper.map(tag, TagDTO.class);
    }

    public List<TagDTO> convertToDTO(List<Tag> tags) {
        List<TagDTO> tagDTOs = new ArrayList<>();
        tags.forEach(tag -> tagDTOs.add(modelMapper.map(tag, TagDTO.class)));
        return tagDTOs;
    }

}
