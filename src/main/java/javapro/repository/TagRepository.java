package javapro.repository;

import javapro.model.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;

@Repository
public interface TagRepository extends JpaRepository<Tag, Integer> {

    @Query("SELECT " +
            "t " +
            "FROM Tag t " +
            "WHERE LOWER(t.tag) LIKE %:searchTag% ")
    Page<Tag> findTagsByText(String searchTag, Pageable pageable);

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
