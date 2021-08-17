package javapro.repository;

import javapro.model.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface NotificationEntityRepository extends JpaRepository<NotificationEntity, Integer> {

    @Override
    Optional<NotificationEntity> findById(Integer integer);

    List<NotificationEntity> findAllByPost(Integer postId);
}
