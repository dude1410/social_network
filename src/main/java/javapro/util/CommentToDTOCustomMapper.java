package javapro.util;

import javapro.model.dto.CommentDTO;
import javapro.model.view.PostCommentView;
import org.mapstruct.Mapper;

import java.util.ArrayList;
import java.util.List;

@Mapper(uses = {DateToSecondsMapper.class})
public interface CommentToDTOCustomMapper {

    default CommentDTO mapper(PostCommentView comment) {
        CommentDTO commentDTO = new CommentDTO();

        commentDTO.setParentCommentID(comment.getParentComment() == null ? null : comment.getParentComment().getId());
        commentDTO.setCommentText(comment.getCommentText());
        commentDTO.setId(comment.getId());
        commentDTO.setPostID(comment.getPost().getId());
        commentDTO.setTime(new DateToSecondsMapper().dateToSecond(comment.getTime()));
        commentDTO.setAuthor(new PersonToDtoMapper().convertToDto(comment.getAuthor()));
        commentDTO.setBlocked(comment.isBlocked());
        commentDTO.setLikes(comment.getCommentLikeList().size());

        return commentDTO;
    }

    default List<CommentDTO> mapper(List<PostCommentView> comments) {
        List<CommentDTO> commentDTOs = new ArrayList<>();
        comments.forEach(comment -> commentDTOs.add(mapper(comment)));
        return commentDTOs;
    }
}
