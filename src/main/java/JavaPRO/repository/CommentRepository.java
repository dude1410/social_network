package JavaPRO.repository;

import JavaPRO.model.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<PostComment, Integer> {

    @Query("SELECT " +
            "pc " +
            "FROM Post p " +
            "LEFT JOIN PostComment pc ON p.id = pc.post.id " +
            "WHERE p.id = :postID")
    List<PostComment> findCommentsByPostID(int postID);

    @Query("SELECT " +
            "pc " +
            "FROM PostComment pc " +
            "WHERE pc.id = :id")
    PostComment findCommentByID(int id);
}
