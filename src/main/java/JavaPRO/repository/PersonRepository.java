package JavaPRO.repository;

import JavaPRO.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {

    @Query("SELECT p " +
            "FROM Person p " +
            "WHERE p.email = :email ")
    Person findByEmail(@Param("email") String email);


    @Query("SELECT p " +
            "FROM Person p " +
            "LEFT JOIN City c ON p.cityId.id = c.id  " +
            "LEFT JOIN Country co ON p.countryId.id = co.id " +
            "WHERE p.email =:email ")
    Person findByEmailForLogin(@Param("email") String email);


}
