package javapro.repository;

import javapro.model.Person;
import javapro.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Transactional
@Repository
public interface TokenRepository extends JpaRepository<Token, Integer> {

    @Query("SELECT " +
            "t " +
            "FROM Token t " +
            "WHERE t.token = :token")
    Token findByToken(@Param("token") String token);

    @Query("SELECT " +
            "t " +
            "FROM Token t " +
            "WHERE t.person = :person")
    Token findByPerson(@Param("person") Person person);

    @Modifying
    @Query("DELETE " +
            "FROM Token t " +
            "WHERE t.date < :date")
    void deleteAllByDateBefore(Date date);
}
