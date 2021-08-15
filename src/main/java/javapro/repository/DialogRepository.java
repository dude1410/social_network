package javapro.repository;

import javapro.model.Dialog;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DialogRepository extends JpaRepository<Dialog, Integer> {

    @Query("select d from Dialog as d " +
            "join Dialog2person as dp on d.id = dp.dialog.id " +
            "join Person as p on p.id = dp.person.id " +
            "where p.id = :personId")
    List<Dialog> findAllPersonDialogs(Pageable pageable, @Param("personId") Integer personId);

    @Query("select d from Dialog as d " +
            "join Dialog2person as dp on d.id = dp.dialog.id " +
            "join Person as p on p.id = dp.person.id " +
            "where p.id = :personId")
    List<Dialog> findAllPersonDialogs(@Param("personId") Integer personId);

    @Query("select count(*) from Dialog as d " +
        "join Dialog2person as dp on d.id = dp.dialog.id " +
        "join Person as p on p.id = dp.person.id " +
        "where p.id = :personId")
    Integer personDialogsCount(@Param("personId") Integer personId);
}
