package javapro.repository;

import javapro.model.view.PostView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface PostViewRepository extends JpaRepository<PostView, Integer> {

    @Query("SELECT " +
            "p " +
            "FROM PostView p " +
            "LEFT JOIN PostToTag ptt " +
            "ON p.id = ptt.post.id " +
            "LEFT JOIN Tag t " +
            "ON ptt.tag.id = t.id " +
            "WHERE LOWER(CONCAT(p.title, p.postText)) LIKE %:searchText%  " +
            "AND LOWER(CONCAT(p.author.firstName, p.author.lastName)) LIKE %:searchAuthor%  " +
            "AND p.time BETWEEN :dateFrom AND :dateTo " +
            "AND (LOWER(COALESCE(t.tag,'')) LIKE :searchTag OR :searchTag = '') " +
            "GROUP BY p.id, " +
            "p.author.id, " +
            "p.isBlocked, " +
            "p.isDeleted, " +
            "p.title, " +
            "p.postText, " +
            "p.time  " +
            "ORDER BY p.time DESC "
    )
    Page<PostView> findPostsByProperties(String searchText, Date dateFrom, Date dateTo, String searchAuthor, String searchTag, Pageable pageable);

    @Query("SELECT " +
            "p " +
            "FROM PostView p " +
            "WHERE LOWER(CONCAT(p.title, p.postText, p.author.firstName, p.author.lastName)) LIKE %:searchText% " +
            "ORDER BY p.time DESC "
    )
    Page<PostView> searchPostBy(String searchText, Pageable pageable);

    @Query("SELECT DISTINCT " +
            "p " +
            "FROM PostView p " +
            "WHERE p.time <= :serverTime " +
            "ORDER BY p.time DESC ")
    Page<PostView> findAllPosts(Date serverTime, Pageable pageable);

    @Query("SELECT " +
            "p " +
            "FROM PostView p " +
            "WHERE p.id = :postID " +
            "ORDER BY p.time DESC ")
    PostView findPostByID(int postID);

    @Query("SELECT " +
            "p " +
            "FROM PostView p " +
            "WHERE p.author.id = :authorID " +
            "ORDER BY p.time DESC ")
    Page<PostView> findPostsByAuthorID(int authorID, Pageable pageable);


    @Query("SELECT " +
            "p.author.id " +
            "FROM PostView p " +
            "WHERE p.id = :postID ")
    Integer getAuthorIDByPostID(int postID);
}
