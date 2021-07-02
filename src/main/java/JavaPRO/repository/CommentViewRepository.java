package JavaPRO.repository;

import JavaPRO.model.CommentsView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentViewRepository extends JpaRepository<CommentsView, Integer> {

}
