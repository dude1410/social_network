package javapro.repository;

import javapro.model.DeletedPerson;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeletedPersonRepository extends JpaRepository<DeletedPerson, Integer> {

}
