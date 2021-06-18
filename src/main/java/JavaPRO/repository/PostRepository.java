package JavaPRO.repository;

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
            //"LEFT JOIN PostComment pc ON p.id = pc.post.id " +
            "WHERE (LOWER(p.title) LIKE %:searchText% OR LOWER(p.postText) LIKE %:searchText%) " +
            "AND p.isDeleted = false " +
            "ORDER BY p.time DESC ")
    List<Post> findPostsByText(String searchText);

    @Query("SELECT " +
            "p " +
            "FROM Post p " +
            "WHERE p.isDeleted = false " +
            "AND p.time <= :serverTime " +
            "ORDER BY p.time DESC ")
    List<Post> findAllPosts(Date serverTime);

    @Query("SELECT " +
            "p " +
            "FROM Post p " +
            "WHERE p.id = :id " +
            "AND p.isDeleted = false " +
            "ORDER BY p.time DESC "
    )
    Post findPostByID(int id);

    @Query("SELECT " +
            "p " +
            "FROM Post p " +
            "WHERE p.author.id = :id " +
            "AND p.isDeleted = false " +
            "ORDER BY p.time DESC "
    )
    List<Post> findPostsByAuthorID(int id);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query("UPDATE Post p set p.isDeleted = true WHERE p.id = :id")
    Integer deletePostByID(int id);

}
