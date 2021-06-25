package JavaPRO.Util;

import JavaPRO.model.DTO.TagDTO;
import JavaPRO.model.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class TagToDTOMapper {
    private final ModelMapper modelMapper;

    public TagToDTOMapper() {

        this.modelMapper = new ModelMapper();
        modelMapper.createTypeMap(Tag.class, TagDTO.class);
    }

    public TagDTO convertToDto(Tag entity) {
        return modelMapper.map(entity, TagDTO.class);
    }
}
