package javapro.util;

import javapro.model.dto.LikeDTO;
import javapro.model.PostLike;
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
