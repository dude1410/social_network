package JavaPRO.Util;

import JavaPRO.model.DTO.PostCommentDTO;
import JavaPRO.model.DTO.PostDTO;
import JavaPRO.model.DTO.PersonDTO;
import JavaPRO.model.Person;
import JavaPRO.model.Post;
import JavaPRO.model.PostComment;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class PostToDTOMapper {

    private final ModelMapper modelMapper;

    public PostToDTOMapper() {
        this.modelMapper = new ModelMapper();

        modelMapper.createTypeMap(Post.class, PostDTO.class);
        modelMapper.createTypeMap(PostComment.class, PostCommentDTO.class);
        modelMapper.createTypeMap(Person.class, PersonDTO.class);
    }

    public PostDTO convertToDTO(Post post) {
        return modelMapper.map(post, PostDTO.class);
    }

}
