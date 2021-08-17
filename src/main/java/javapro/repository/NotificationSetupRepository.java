package javapro.repository;

import javapro.model.NotificationSetup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationSetupRepository extends JpaRepository<NotificationSetup, Long> {


    List<NotificationSetup> findAllByPersonId(Integer personId);

    NotificationSetup findByNotificationtypeAndPersonId(String notificationType, Integer personId);

    @Modifying
    @Query("delete " +
            "from NotificationSetup ns " +
            "where ns.personId = :personId")
    void deleteNotificationSetupsByPersonId(@Param("personId") Integer personId);

}
