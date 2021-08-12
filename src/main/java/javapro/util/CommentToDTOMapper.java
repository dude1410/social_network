package javapro.util;

import javapro.model.dto.CommentDTO;
import javapro.model.PostComment;
import javapro.model.view.PostCommentView;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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

    public List<CommentDTO> convertToDTO(List<PostCommentView> comments) {
        List<CommentDTO> commentDTOs = new ArrayList<>();
        comments.forEach(comment -> commentDTOs.add(modelMapper.map(comment, CommentDTO.class)));
        return commentDTOs;
    }
}
