package JavaPRO.repository;

import JavaPRO.model.Person;
import JavaPRO.model.PersonView;
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
            "WHERE p.isDeleted = false " +
            "AND p.isBlocked = false " +
            "AND LOWER(CONCAT(p.title, p.postText)) LIKE %:searchText% " +
            "AND LOWER(CONCAT(p.author.firstName, p.author.lastName)) LIKE %:searchAuthor% " +
            "AND p.time BETWEEN :dateFrom AND :dateTo " +
            "ORDER BY p.time DESC "
    )
    List<Post> findPostsByProperties(String searchText , Date dateFrom, Date dateTo, String searchAuthor);

    @Query("SELECT " +
            "pv " +
            "FROM PersonView pv " +
            "WHERE LOWER(COALESCE(pv.firstName,''))     LIKE %:firstName% " +
            "AND LOWER(COALESCE(pv.lastName,''))        LIKE %:lastName% " +
            "AND (COALESCE(pv.age, 0)                   BETWEEN :ageFrom AND :ageTo) " +
            "AND LOWER(COALESCE(pv.countryName,''))     LIKE %:country% " +
            "AND LOWER(COALESCE(pv.townName,''))        LIKE %:town% "
    )
    List<PersonView> findPersonsByProperties(String firstName, String lastName, Integer ageFrom, Integer ageTo, String country, String town);


    @Query("SELECT DISTINCT " +
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
            "p.author.id " +
            "FROM Post p " +
            "WHERE p.id = :postID ")
    Integer getAuthorIDByPostID(int postID);
}
