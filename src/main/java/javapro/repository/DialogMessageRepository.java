package javapro.repository;

import javapro.model.DialogMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DialogMessageRepository extends JpaRepository<DialogMessage, Integer> {
}
