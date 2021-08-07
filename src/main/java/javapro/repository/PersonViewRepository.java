package javapro.repository;

import javapro.model.PersonView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PersonViewRepository extends JpaRepository<PersonView, Integer> {

    @Query("SELECT " +
            "pv " +
            "FROM PersonView pv " +
            "WHERE LOWER(COALESCE(pv.firstName,''))     LIKE %:firstName% " +
            "AND LOWER(COALESCE(pv.lastName,''))        LIKE %:lastName% " +
            "AND (COALESCE(pv.age, 0)                   BETWEEN :ageFrom AND :ageTo) " +
            "AND LOWER(COALESCE(pv.countryName,''))     LIKE %:country% " +
            "AND LOWER(COALESCE(pv.townName,''))        LIKE %:town% "
    )
    Page<PersonView> findPersonsByProperties(String firstName, String lastName, Integer ageFrom, Integer ageTo, String country, String town, Pageable pageable);

    @Query("SELECT " +
            "pv " +
            "FROM PersonView pv " +
            "WHERE LOWER(COALESCE(pv.firstName,pv.lastName,'')) LIKE %:searchText% "
    )
    Page<PersonView> searchPersonBy(String searchText, Pageable pageable);
}
