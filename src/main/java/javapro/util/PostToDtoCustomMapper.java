package javapro.util;

import javapro.model.view.PostView;
import javapro.model.dto.PostDTO;
import javapro.model.enums.PostStatus;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.sql.Timestamp;

@Mapper(uses = {DateToSecondsMapper.class})
public interface PostToDtoCustomMapper {

    default PostDTO mapper(PostView post) {
        PostDTO postDTO = new PostDTO();

        postDTO.setId(post.getId());
        postDTO.setTime(new DateToSecondsMapper().dateToSecond(post.getTime()));
        postDTO.setAuthor(new PersonToDtoMapper().convertToDto(post.getAuthor()));
        postDTO.setTitle(post.getTitle());
        postDTO.setPostText(post.getPostText());
        postDTO.setIsBlocked(post.isBlocked());
        postDTO.setLikes(post.getPostLikeList().size());
        postDTO.setPostComments(Mappers.getMapper(CommentToDTOCustomMapper.class).mapper(post.getPostCommentList()));
        postDTO.setTags(new TagToDTOMapper().convertToDTO(post.getPostTagList()));

        postDTO.setPostStatus((postDTO.getTime() > new Timestamp(System.currentTimeMillis()).getTime() ? PostStatus.QUEUED : PostStatus.POSTED).toString());

        return postDTO;
    }
}
