package javapro.repository;

import javapro.model.Dialog2person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface Dialog2PersonRepository extends JpaRepository<Dialog2person, Integer> {

    @Query("select d2p " +
            "from Dialog2person d2p " +
            "WHERE d2p.person.id = :personId " +
            "AND d2p.dialog.id = :dialogId")
    Dialog2person findByDialogIdAndPersonId(@Param("dialogId") Integer dialogId, @Param("personId") Integer personId);
}
