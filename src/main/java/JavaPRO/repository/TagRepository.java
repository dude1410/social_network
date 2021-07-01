package JavaPRO.repository;

import JavaPRO.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, Integer> {

    @Query("SELECT " +
            "t " +
            "FROM Tag t " +
            "WHERE LOWER(t.tag) LIKE %:searchTag% ")
    List<Tag> findTagsByText(String searchTag);

    @Query("SELECT " +
            "t " +
            "FROM Tag t " +
            "WHERE t.tag = :searchTag ")
    Tag findTagByName(String searchTag);


    @Query("SELECT " +
            "t " +
            "FROM Tag t " +
            "WHERE t.id = :tagID ")
    Tag findTagByID(Integer tagID);
}
