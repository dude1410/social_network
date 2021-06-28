package JavaPRO.repository;

import JavaPRO.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

@Repository
@Transactional
public interface PersonRepository extends JpaRepository<Person, Integer> {

    @Query("SELECT p " +
            "FROM Person p " +
            "WHERE p.email = :email ")
    Person findByEmail(@Param("email") String email);

    @Query("SELECT p " +
            "FROM Person p " +
            "LEFT JOIN Town t ON p.townId.id = t.id  " +
            "LEFT JOIN Country co ON p.countryId.id = co.id " +
            "WHERE p.email =:email ")
    Person findByEmailForLogin(@Param("email") String email);

    @Query("SELECT p " +
            "FROM Person p " +
            "WHERE p.confirmationCode = :code")
    Person findByCode(@Param("code") String code);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query("UPDATE Person p " +
            "SET p.isApproved = true " +
            "WHERE p.confirmationCode = :token")
    Integer setIsApprovedTrue(@Param("token") String token);

    Person findByConfirmationCode(String confirmationCode);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query("UPDATE Person p " +
            "SET p.password = :newPassword " +
            "WHERE p.confirmationCode = :code")
    Integer setNewPassword(@Param("newPassword") String newPassword, @Param("code") String code);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query("UPDATE Person p " +
            "SET p.password = :newPassword " +
            "WHERE p.email = :email")
    Integer changePassword(@Param("newPassword") String newPassword, @Param("email") String email);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query("UPDATE Person p " +
            "SET p.email = :newEmail " +
            "WHERE p.email = :oldEmail")
    Integer changeEmail(@Param("oldEmail") String oldEmail, @Param("newEmail") String newEmail);

    @Modifying
    @Query("DELETE " +
            "from Person p " +
            "WHERE p.isApproved = false " +
            "AND p.regDate < :timestamp")
    void deleteAllByRegDateBefore(Timestamp timestamp);

    @Query("select p.id " +
            "from Person p " +
            "where p.email = :email ")
    int findUserIdByEmail(@Param("email") String email);

    @Query("select p " +
            "from Person p " +
            "left join Friendship f on p.id=f.dstPersonId.id " +
            "left join Friendship fr on p.id=fr.srcPersonId.id " +
            "where (f.status = 'FRIEND' and f.srcPersonId.id = :userId) " +
            "or (fr.status = 'FRIEND' and fr.dstPersonId.id = :userId) " +
            "group by p ")
    List<Person> findAllFriends(int userId);

    @Query("select p " +
            "from Person p " +
            "left join Friendship f on p.id=f.dstPersonId.id " +
            "left join Friendship fr on p.id=fr.srcPersonId.id " +
            "where ((f.status = 'FRIEND' and f.srcPersonId.id = :userId) and (p.firstName like %:name% or p.lastName like %:name%)) " +
            "or ((fr.status = 'FRIEND' and fr.dstPersonId.id = :userId) and (p.firstName like %:name% or p.lastName like %:name%)) " +
            "group by p ")
    List<Person> findAllFriendsByName(@Param("name") String name, @Param("userId") Integer userId);

    @Query("select p " +
            "from Person p " +
            "where p.id = :id ")
    Person findPersonById(@Param("id") Integer id);

    @Query("select p " +
            "from Person p " +
            "left join Friendship f on f.srcPersonId.id = p.id " +
            "where f.dstPersonId.id = :id and f.status = 'REQUEST' " +
            "group by p ")
    List<Person> findAllRequestsById(@Param("id") Integer id);

    @Query("select p " +
            "from Person p " +
            "left join Friendship f on f.srcPersonId.id = p.id " +
            "where f.dstPersonId.id = :id and " +
            "(f.srcPersonId.lastName like %:name% or f.srcPersonId.firstName like %:name% ) " +
            "and f.status = 'REQUEST' " +
            "group by p ")
    List<Person> findAllRequestsByIdAndName(@Param("id") Integer id, @Param("name") String name);

    @Query("select p " +
            "from Person p " +

            "left join Friendship dst_dst on dst_dst.srcPersonId.id = p.id " +
            "left join Friendship dst_src on dst_src.dstPersonId.id = p.id " +
            "left join Friendship dst1 on dst1.dstPersonId.id = dst_dst.dstPersonId.id " +
            "left join Friendship dst2 on dst2.dstPersonId.id = dst_src.srcPersonId.id " +

            "left join Friendship src_src on src_src.dstPersonId.id = p.id " +
            "left join Friendship src_dst on src_dst.srcPersonId.id = p.id " +
            "left join Friendship src1 on src1.srcPersonId.id = src_src.srcPersonId.id " +
            "left join Friendship src2 on src2.srcPersonId.id = src_dst.dstPersonId.id " +

            "where (dst_dst.status = 'FRIEND' and dst1.status = 'FRIEND' and dst1.srcPersonId.id = :userId and p.id != :userId) " +
            "or (dst_src.status = 'FRIEND' and dst2.status = 'FRIEND' and dst2.srcPersonId.id = :userId and p.id != :userId) " +
            "or (src_src.status = 'FRIEND' and src1.status = 'FRIEND' and src1.dstPersonId.id = :userId and p.id != :userId) " +
            "or (src_dst.status = 'FRIEND' and src2.status = 'FRIEND' and src2.dstPersonId.id = :userId and p.id != :userId) " +
            "group by p ")
    List<Person> findRecommendations(@Param("userId") Integer userId);

}
