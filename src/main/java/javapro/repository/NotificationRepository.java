package javapro.repository;

import javapro.model.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {


    @Query("select n " +
            "from Notification n " +
            "left join NotificationEntity ne ON ne.id = n.entity.id " +
            "where n.person.id = :id ")
    Page<Notification> findAllByPersonId(Pageable pageable, @Param("id") Integer id);
}
