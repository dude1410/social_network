package javapro.repository;

import javapro.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

    @Query("SELECT " +
            "p " +
            "FROM Post p " +
            "WHERE p.isDeleted = false " +
            "AND p.isBlocked = false " +
            "AND LOWER(CONCAT(p.title, p.postText)) LIKE %:searchText% " +
            "AND LOWER(CONCAT(p.author.firstName, p.author.lastName)) LIKE %:searchAuthor% " +
            "AND p.time BETWEEN :dateFrom AND :dateTo " +
            "ORDER BY p.time DESC "
    )
    Page<Post> findPostsByProperties(String searchText, Date dateFrom, Date dateTo, String searchAuthor, Pageable pageable);

    @Query("SELECT " +
            "p " +
            "FROM Post p " +
            "WHERE p.isDeleted = false " +
            "AND p.isBlocked = false " +
            "AND LOWER(CONCAT(p.title, p.postText, p.author.firstName, p.author.lastName)) LIKE %:searchText% "
    )
    Page<Post> searchPostBy(String searchText, Pageable pageable);

    @Query("SELECT DISTINCT " +
            "p " +
            "FROM Post p " +
            "WHERE p.isDeleted = false " +
            "AND p.isBlocked = false " +
            "AND p.time <= :serverTime " +
            "ORDER BY p.time DESC ")
    Page<Post> findAllPosts(Date serverTime, Pageable pageable);

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
    Page<Post> findPostsByAuthorID(int authorID, Pageable pageable);


    @Query("SELECT " +
            "p.author.id " +
            "FROM Post p " +
            "WHERE p.id = :postID ")
    Integer getAuthorIDByPostID(int postID);
}
