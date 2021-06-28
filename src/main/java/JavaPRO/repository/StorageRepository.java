package JavaPRO.repository;

import JavaPRO.model.DTO.Storage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface StorageRepository extends JpaRepository<Storage, Long> {



}
