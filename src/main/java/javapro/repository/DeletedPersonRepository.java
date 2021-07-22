package javapro.repository;

import javapro.model.DeletedPerson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeletedPersonRepository extends JpaRepository<DeletedPerson, Integer> {


    Optional<DeletedPerson> findByPersonId(Integer personId);
}
