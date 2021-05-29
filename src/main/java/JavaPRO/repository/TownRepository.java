package JavaPRO.repository;

import JavaPRO.model.City;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TownRepository extends JpaRepository<City, Integer> {
}
