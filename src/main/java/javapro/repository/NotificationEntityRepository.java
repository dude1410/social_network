package javapro.repository;

import javapro.model.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface NotificationEntityRepository extends JpaRepository<NotificationEntity, Integer> {

    @Override
    Optional<NotificationEntity> findById(Integer integer);

    @Query("select ne " +
            "from NotificationEntity ne " +
            "WHERE ne.post.id = :postId ")
    List<NotificationEntity> findAllByPost(@Param("postId") Integer postId);

    @Query("select ne " +
            "from NotificationEntity ne " +
            "where ne.postComment.id = :postCommenId")
    List<NotificationEntity> findAllByPostComment(@Param("postCommenId") Integer postCommenId);


}
