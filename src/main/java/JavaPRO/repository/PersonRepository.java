package JavaPRO.repository;

import JavaPRO.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {

    @Query("SELECT p " +
            "FROM Person p " +
            "WHERE p.email = :email ")
    Person findByEmail(@Param("email") String email);


    @Query("SELECT p " +
            "FROM Person p " +
            "LEFT JOIN Town t ON p.townId.id = t.id  " +
            "LEFT JOIN Country co ON p.countryId.id = co.id " +
            "WHERE p.email =:email ")
    Person findByEmailForLogin(@Param("email") String email);



    @Query("SELECT p " +
            "FROM Person p " +
            "WHERE p.id = :id " +
            "AND p.confirmationCode = :code")
    Person findByIdAndCode(@Param("id") int id, @Param("code") String code);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query("UPDATE Person p SET p.isApproved = true WHERE p.id = :id")
    Integer setIsApprovedTrue(@Param("id") int id);

    Person findByConfirmationCode(String confirmationCode);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query("UPDATE Person p SET p.password = :newPassword WHERE p.confirmationCode = :code")
    Integer setNewPassword(@Param("newPassword") String newPassword, @Param("code") String code);
}
