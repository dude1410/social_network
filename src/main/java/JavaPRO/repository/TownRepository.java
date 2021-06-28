package JavaPRO.repository;

import JavaPRO.model.Town;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TownRepository extends JpaRepository<Town, Integer> {

    Optional<Town> findByName(String name);
}
