package JavaPRO.Util;

import JavaPRO.model.DTO.LikeDTO;
import JavaPRO.model.PostLike;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class LikeToDTOMapper {

    private final ModelMapper modelMapper;

    public LikeToDTOMapper() {

        this.modelMapper = new ModelMapper();
        modelMapper.createTypeMap(PostLike.class, LikeDTO.class);
    }

    public LikeDTO convertToDto(PostLike entity) {
        return modelMapper.map(entity, LikeDTO.class);
    }
}
