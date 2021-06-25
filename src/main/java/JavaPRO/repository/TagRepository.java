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
    List<Tag> findTagByText(String searchTag);
}
