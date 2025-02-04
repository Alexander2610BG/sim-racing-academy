package aleksandarskachkov.simracingacademy.module.repository;

import aleksandarskachkov.simracingacademy.module.model.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ModuleRepository extends JpaRepository<Module, UUID> {
}
