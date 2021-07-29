package javapro.repository;

import javapro.model.DeletedPerson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DeletedPersonRepository extends JpaRepository<DeletedPerson, Integer> {


    Optional<DeletedPerson> findByPersonId(Integer personId);

    @Query("select p from DeletedPerson p WHERE p.personId = :personId")
    DeletedPerson findPerson(@Param("personId") Integer personId);



    @Override
    List<DeletedPerson> findAll();
}
