package JavaPRO.Util;

import JavaPRO.model.DTO.CommentDTO;
import JavaPRO.model.PostComment;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class CommentToDTOMapper {

    private final ModelMapper modelMapper;

    public CommentToDTOMapper() {
        this.modelMapper = new ModelMapper();
        modelMapper.createTypeMap(PostComment.class, CommentDTO.class);
    }

    public CommentDTO convertToDTO(PostComment comment) {
        return modelMapper.map(comment, CommentDTO.class);
    }

}
