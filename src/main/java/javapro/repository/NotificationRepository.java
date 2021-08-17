package javapro.repository;

import javapro.model.Notification;
import javapro.model.enums.NotificationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {


    @Query("select n " +
            "from Notification n " +
            "left join NotificationEntity ne ON ne.id = n.entity.id " +
            "where n.person.id = :id ")
    Page<Notification> findAllByPersonId(Pageable pageable, @Param("id") Integer id);

    @Modifying
    @Query("delete " +
            "from Notification n " +
            "where n.person.id = :personId")
    void deleteAll(@Param("personId") Integer personId);

    @Query("select n " +
            "from Notification n " +
            "left join NotificationEntity ne ON ne.id = n.entity.id " +
            "where n.person.id = :id ")
    List<Notification> findAllByPId(@Param("id") Integer id);

    @Modifying
    @Query("delete " +
            "from Notification n " +
            "where n.entity.person.id = :id ")
    void deleteAllByAuthorId(@Param("id") Integer id);

    @Modifying
    void deleteAllByNotificationType(NotificationType notificationType);

    List<Notification> findAllByPersonId(Integer personId);

    @Query("select n " +
            "FROM Notification n " +
            "where n.entity.id = :entityId ")
    List<Notification> findAllByEntity(@Param("entityId") Integer entityId);

    @Modifying
    @Query("delete " +
            "from Notification n " +
            "where n.entity.id = :entity " +
            "AND n.notificationType = :notificationType ")
    void deleteAllNotificationFromDeletedPost(@Param("entity") Integer entity,
                                              @Param("notificationType") NotificationType notificationType);

}
