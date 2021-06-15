package JavaPRO.repository;

import JavaPRO.model.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostCommentRepository extends JpaRepository<PostComment, Integer> {
    /*
    @Query("SELECT " +
            "pc.parentComment.id AS parent_id, " +
            "pc.commentText AS comment_text, " +
            "pc.id AS id, " +
            "pc.post.id AS post_id, " +
            "0 AS time, " +
            "pc.author.id AS author_id, " +
            "pc.isBlocked AS is_blocked " +
            "FROM Post p " +
            "LEFT JOIN PostComment pc ON p.id = pc.post.id " +
            "WHERE p.id = :postID")
    List<PostComment> findCommentsByPostID(int postID);
    */
}
