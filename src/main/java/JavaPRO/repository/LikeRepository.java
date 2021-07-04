package JavaPRO.repository;

import JavaPRO.model.Person;
import JavaPRO.model.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface LikeRepository extends JpaRepository<PostLike, Integer> {

    @Query("SELECT DISTINCT " +
            "pl.id " +
            "FROM Person p " +
            "INNER JOIN PostLike pl " +
            "ON p.id = pl.post.id " +
            "WHERE p.email = :email " +
            "AND pl.post.id = :postID ")
    Integer userLikedPost(String email, Integer postID);

    @Query("SELECT " +
            "COUNT(pl.id) " +
            "FROM PostLike pl " +
            "WHERE pl.post.id = :postID " +
            "AND pl.person.id = :userID ")
    Integer isUserLikedPost(int userID, int postID);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query("DELETE " +
            "FROM PostLike pl " +
            "WHERE pl.post.id = :postID " +
            "AND pl.person.id = :userID ")
    void deleteLikeOnPost(int userID, int postID);

    @Query("SELECT " +
            "COUNT(pl.id) " +
            "FROM PostLike pl " +
            "WHERE pl.post.id = :postID ")
    Integer getLikes(int postID);

    @Query("SELECT DISTINCT " +
            "prsn " +
            "FROM PostLike pl " +
            "LEFT JOIN Post p ON pl.post.id = p.id " +
            "LEFT JOIN Person prsn ON pl.person.id = prsn.id " +
            "WHERE pl.post.id = :postID ")
    List<Person> getUsersWhoLikedPost(int postID);

}
