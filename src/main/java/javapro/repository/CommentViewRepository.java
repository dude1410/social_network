package javapro.repository;

import javapro.model.CommentsView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentViewRepository extends JpaRepository<CommentsView, Integer> {

}
