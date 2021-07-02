package JavaPRO.Util;

import JavaPRO.model.CommentsView;
import JavaPRO.model.DTO.CommentViewDTO;
import JavaPRO.model.DTO.PostDTO;
import JavaPRO.model.Post;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class PostToDTOMapper {

    private final ModelMapper modelMapper;

    public PostToDTOMapper() {
        this.modelMapper = new ModelMapper();

        modelMapper.createTypeMap(Post.class, PostDTO.class);
        modelMapper.createTypeMap(CommentsView.class, CommentViewDTO.class);
    }

    public PostDTO convertToDTO(Post post) {
        return modelMapper.map(post, PostDTO.class);
    }
}
