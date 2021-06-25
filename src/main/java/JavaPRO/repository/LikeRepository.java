package JavaPRO.repository;

import JavaPRO.model.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LikeRepository extends JpaRepository<PostLike, Integer> {

    @Query("SELECT DISTINCT " +
            "pl.id " +
            "FROM Person p " +
            "INNER JOIN PostLike pl " +
            "ON p.id = pl.post.id " +
            "WHERE p.email = :email " +
            "AND pl.post.id = :postID ")
    Integer userLikedPost(String email, Integer postID);


}
