package aleksandarskachkov.simracingacademy.module;

import aleksandarskachkov.simracingacademy.module.model.Module;
import aleksandarskachkov.simracingacademy.module.model.ModuleName;
import aleksandarskachkov.simracingacademy.module.model.ModuleType;
import aleksandarskachkov.simracingacademy.module.repository.ModuleRepository;
import aleksandarskachkov.simracingacademy.module.service.ModuleService;
import aleksandarskachkov.simracingacademy.user.model.User;
import aleksandarskachkov.simracingacademy.video.model.Video;
import aleksandarskachkov.simracingacademy.video.service.VideoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ModuleServiceUTest {

    @Mock
    private ModuleRepository moduleRepository;
    @Mock
    private VideoService videoService;

    @InjectMocks
    private ModuleService moduleService;

    @Test
    void givenModuleDetails_whenCreateModule_thenSaveModule() {
        // Given

        Video video = Video.builder()
                .id(UUID.randomUUID())
                .title("Title")
                .description("Description")
                .build();

        User owner = new User();

        Module module = Module.builder()
                .id(UUID.randomUUID())
                .name(ModuleName.FOUNDATIONS_OF_SIM_RACING)
                .description("Introduces the core principles of sim racing")
                .type(ModuleType.SUBSCRIPTION)
                .videos(List.of(video))
                .owner(owner)
                .build();

        when(moduleRepository.save(any(Module.class))).thenReturn(module);

        // When
        Module result = moduleService.createModule(module.getName(), module.getDescription(), module.getType(), module.getVideos());

        // Then
        assertEquals(module.getName(), result.getName());
        assertEquals(module.getDescription(), result.getDescription());
        assertEquals(module.getType(), result.getType());
        assertEquals(module.getVideos().size(), result.getVideos().size());
        assertEquals(module.getOwner(), result.getOwner());
        verify(moduleRepository, times(1)).save(any(Module.class));
    }

    @Test
    void givenModuleId_whenGetModuleById_thenReturnModule() {
        // Given
        Module module = Module.builder()
                .id(UUID.randomUUID())
                .build();
        when(moduleRepository.getById(module.getId())).thenReturn(module);

        // When
        Module result = moduleService.getModuleById(module.getId());

        // Then
        assertNotNull(result);
        assertEquals(module, result);
        verify(moduleRepository, times(1)).getById(module.getId());
    }

    @Test
    void givenNonExistingModuleName_whenAddModuleIfNotExists_thenSaveModule() {
        // Given
        ModuleName moduleName = ModuleName.RACECRAFT_AND_STRATEGY;
        String description = "Covers the art of competitive racing";
        ModuleType type = ModuleType.SUBSCRIPTION;
        when(moduleRepository.findByName(moduleName)).thenReturn(null);

        // When
        moduleService.addModuleIfNotExists(moduleName, description, type);

        // Then
        verify(moduleRepository, times(1)).save(any(Module.class));
    }

    @Test
    void givenExistingModuleName_whenAddModuleIfExists_thenDoNotSaveNewModule() {

        // Given
        when(moduleRepository.findByName(ModuleName.FOUNDATIONS_OF_SIM_RACING)).thenReturn(new Module());

        // When
        moduleService.addModuleIfNotExists(ModuleName.FOUNDATIONS_OF_SIM_RACING, "", ModuleType.SUBSCRIPTION);

        // Then
        verify(moduleRepository, never()).save(any(Module.class));
    }

    @Test
    void givenOwnerId_whenGetAllModules_thenReturnModuleList() {

        // Given
        UUID ownerId = UUID.randomUUID();
        List<Module> modules = List.of(new Module(), new Module());
        when(moduleRepository.findAllByOwnerId(ownerId)).thenReturn(modules);

        // When
        List<Module> result = moduleService.getAllModules(ownerId);

        // Then
        assertEquals(modules.size(), result.size());
        verify(moduleRepository, times(1)).findAllByOwnerId(ownerId);
    }

}
