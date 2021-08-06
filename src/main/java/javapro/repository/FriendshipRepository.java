package javapro.repository;

import javapro.model.Friendship;
import javapro.model.enums.FriendshipStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface FriendshipRepository extends JpaRepository<Friendship, Integer> {

    @Query("select f " +
            "from Friendship f " +
            "where (f.srcPersonId.id = :userId and f.dstPersonId.id = :friendId) " +
            "or (f.srcPersonId.id = :friendId and f.dstPersonId.id = :userId) ")
    Friendship findFriendshipByUsers(@Param("userId") Integer userId,
                                     @Param("friendId") Integer friendId);

    @Query("select f " +
            "from Friendship f " +
            "where (f.srcPersonId.id = :friendId and f.dstPersonId.id = :userId) " +
            "and f.status = 'REQUEST' ")
    Friendship findFriendshipRequest(@Param("userId") Integer userId,
                                     @Param("friendId") Integer friendId);

    @Query("select f " +
            "from Friendship f " +
            "where (f.srcPersonId.id = :userId and f.dstPersonId = :friendId) " +
            "or (f.srcPersonId.id = :friendId and f.dstPersonId = :userId) ")
    Friendship findStatusByIds(@Param("userId")Integer userId,
                               @Param("friendId") Integer friendId);


    @Query("select f " +
            "from Friendship f " +
            "LEFT join Person p ON f.dstPersonId = p.id " +
            "LEFT join Person p ON f.srcPersonId = p.id " +
            "where f.status = :status")
    List<Friendship> findAllByStatus(@Param("status") FriendshipStatus status);
}
