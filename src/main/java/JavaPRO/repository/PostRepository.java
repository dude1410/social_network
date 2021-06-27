package JavaPRO.repository;

import JavaPRO.model.Person;
import JavaPRO.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

    @Query("SELECT " +
            "p " +
            "FROM Post p " +
            "WHERE (LOWER(p.title) LIKE %:searchText% OR LOWER(p.postText) LIKE %:searchText%) " +
            "AND p.isDeleted = false " +
            "AND p.isBlocked = false " +
            "ORDER BY p.time DESC ")
    List<Post> findPostsByText(String searchText);

    @Query("SELECT " +
            "p " +
            "FROM Post p " +
            "WHERE p.isDeleted = false " +
            "AND p.isBlocked = false " +
            "AND p.time <= :serverTime " +
            "ORDER BY p.time DESC ")
    List<Post> findAllPosts(Date serverTime);

    @Query("SELECT " +
            "p " +
            "FROM Post p " +
            "WHERE p.id = :postID " +
            "AND p.isDeleted = false " +
            "AND p.isBlocked = false " +
            "ORDER BY p.time DESC ")
    Post findPostByID(int postID);

    @Query("SELECT " +
            "p " +
            "FROM Post p " +
            "WHERE p.author.id = :authorID " +
            "AND p.isDeleted = false " +
            "AND p.isBlocked = false " +
            "ORDER BY p.time DESC ")
    List<Post> findPostsByAuthorID(int authorID);


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

    @Query("SELECT " +
            "p.author.id " +
            "FROM Post p " +
            "WHERE p.id = :postID ")
    Integer getAuthorIDByPostID(int postID);
}
