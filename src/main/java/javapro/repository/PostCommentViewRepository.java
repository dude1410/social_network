package javapro.repository;

import javapro.model.view.PostCommentView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PostCommentViewRepository extends JpaRepository<PostCommentView, Integer> {

    @Query("SELECT " +
            "pc " +
            "FROM PostCommentView pc " +
            "WHERE pc.post.id = :postID " +
            "ORDER BY pc.time DESC")
    Page<PostCommentView> findCommentsByPostID(int postID, Pageable pageable);

    @Query("SELECT " +
            "pc " +
            "FROM PostCommentView pc " +
            "WHERE pc.id = :commentID ")
    PostCommentView findCommentByID(int commentID);


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
    Integer getLikesOnComment(Integer commentID);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query("DELETE " +
            "FROM PostLike pl " +
            "WHERE pl.comment.id = :commentID " +
            "AND pl.person.id = :userID ")
    void deleteLikeOnComment(Integer userID, Integer commentID);

}
