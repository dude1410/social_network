package JavaPRO.repository;

import JavaPRO.model.Person;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {

    @Query("SELECT p " +
            "FROM Person p " +
            "WHERE p.email = :email ")
    Person findByEmail(@Param("email") String email);

}
