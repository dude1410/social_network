package JavaPRO.repository;

import JavaPRO.model.Person;
import JavaPRO.model.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<PostComment, Integer> {

    @Query("SELECT " +
            "pc " +
            "FROM Post p " +
            "LEFT JOIN PostComment pc ON p.id = pc.post.id " +
            "WHERE p.id = :postID " +
            "AND pc.isDeleted = false " +
            "AND pc.isBlocked = false ")
    List<PostComment> findCommentsByPostID(int postID);

    @Query("SELECT " +
            "pc " +
            "FROM PostComment pc " +
            "WHERE pc.id = :commentID " +
            "AND pc.isDeleted = false " +
            "AND pc.isBlocked = false ")
    PostComment findCommentByID(int commentID);


    @Query("SELECT " +
            "COUNT(pl.id) " +
            "FROM PostLike pl " +
            "WHERE pl.comment.id = :commentID " +
            "AND pl.person.id = :userID ")
    Integer isUserLikedComment(Integer userID, Integer commentID);

    @Query("SELECT " +
            "COUNT(pl.id) " +
            "FROM PostLike pl " +
            "WHERE pl.comment.id = :commentID ")
    Integer getLikes(Integer commentID);

    @Query("SELECT DISTINCT " +
            "prsn " +
            "FROM PostLike pl " +
            "LEFT JOIN PostComment p ON pl.comment.id = p.id " +
            "LEFT JOIN Person prsn ON pl.person.id = prsn.id " +
            "WHERE pl.comment.id = :commentID ")
    List<Person> getUsersWhoLikedComment(Integer commentID);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query("DELETE " +
            "FROM PostLike pl " +
            "WHERE pl.comment.id = :commentID " +
            "AND pl.person.id = :userID ")
    void deleteLikeOnComment(Integer userID, Integer commentID);
}
