package javapro.util;

import javapro.model.PostComment;
import javapro.model.dto.CommentDTO;
import org.mapstruct.Mapper;

@Mapper(uses = {DateToSecondsMapper.class})
public interface CommentToDTOCustomMapper {

    default CommentDTO mapper(PostComment comment) {
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
}
