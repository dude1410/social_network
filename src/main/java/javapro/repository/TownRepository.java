package javapro.repository;

import javapro.model.Town;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TownRepository extends JpaRepository<Town, Integer> {

    Optional<Town> findByName(String name);

    Optional<Town> findById(Integer id);

    @Query("select t " +
            "from Town t " +
            "where t.countryId = :countryId ")
    Page<Town> findAll(Pageable pageable , @Param("countryId") Integer countryId);

    @Query("select t " +
            "from Town t " +
            "WHERE t.name = :id " +
            "AND t.countryId = :countryId ")
    Page<Town> findOne (Pageable pageable, @Param("countryId") Integer countryId, @Param("id") Integer id);
}
