package aleksandarskachkov.simracingacademy.module.repository;

import aleksandarskachkov.simracingacademy.module.model.Module;
import aleksandarskachkov.simracingacademy.module.model.ModuleName;
import aleksandarskachkov.simracingacademy.module.model.ModuleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ModuleRepository extends JpaRepository<Module, UUID> {
    Module findByName(ModuleName name);

    List<Module> findAllByOwnerId(UUID ownerId);

    List<Module> getAllByType(ModuleType type);
}
