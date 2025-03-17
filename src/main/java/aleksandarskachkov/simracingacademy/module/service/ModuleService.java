package aleksandarskachkov.simracingacademy.module.service;

import aleksandarskachkov.simracingacademy.module.model.Module;
import aleksandarskachkov.simracingacademy.module.model.ModuleName;
import aleksandarskachkov.simracingacademy.module.model.ModuleType;
import aleksandarskachkov.simracingacademy.module.repository.ModuleRepository;
import aleksandarskachkov.simracingacademy.video.model.Video;
import aleksandarskachkov.simracingacademy.video.service.VideoService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class ModuleService {

    private final ModuleRepository moduleRepository;
    private final VideoService videoService;

    @Autowired
    public ModuleService(ModuleRepository moduleRepository, VideoService videoService) {
        this.moduleRepository = moduleRepository;
        this.videoService = videoService;
    }

    public Module createModule(ModuleName moduleName, String description, ModuleType type, List<Video> videos) {

        Module module = Module.builder()
                .name(moduleName)
                .description(description)
                .type(type)
                .videos(videos)
                .build();

        log.info("Module created: {}", module);

        return moduleRepository.save(module);
    }

    private void addModuleIfNotExists(ModuleName moduleName, String description, ModuleType type) {
        Optional<Module> existingTrack = Optional.ofNullable(moduleRepository.findByName(moduleName));
        if (existingTrack.isPresent()) {
            log.info("Module already exists: {} Skipping...", existingTrack.get());
        } else {
            createModule(moduleName, description, type, List.of());
        }
    }

    @PostConstruct
    public void initializeModules() {
        List<ModuleName> moduleNames = List.of(
                ModuleName.FOUNDATIONS_OF_SIM_RACING,
                ModuleName.ADVANCED_DRIVING_TECHNIQUES,
                ModuleName.RACECRAFT_AND_STRATEGY,
                ModuleName.TELEMETRY_AND_DATA_ANALYSIS,
                ModuleName.VEHICLE_SETUP_OPTIMIZATION,
                ModuleName.MENTAL_TOUGHNESS_AND_FOCUS
        );

        for (ModuleName moduleName : moduleNames) {
            switch (moduleName) {
                case FOUNDATIONS_OF_SIM_RACING -> addModuleIfNotExists(
                        ModuleName.FOUNDATIONS_OF_SIM_RACING,
                        "Introduces the core principles of sim racing, including car control, racing lines, braking techniques, and throttle management. Ideal for beginners looking to build a solid foundation in virtual motorsports.",
                        ModuleType.DEFAULT
                );
                case ADVANCED_DRIVING_TECHNIQUES -> addModuleIfNotExists(
                        ModuleName.ADVANCED_DRIVING_TECHNIQUES,
                        "Focuses on refining driving skills such as trail braking, throttle modulation, weight transfer, and cornering dynamics. This module helps drivers maximize performance and efficiency on the track.",
                        ModuleType.SUBSCRIPTION
                );
                case RACECRAFT_AND_STRATEGY -> addModuleIfNotExists(
                        ModuleName.RACECRAFT_AND_STRATEGY,
                        "Covers the art of competitive racing, including overtaking strategies, defensive driving, race positioning, and tactical decision-making. Learn how to read opponents and adapt strategies for different race conditions.",
                        ModuleType.SUBSCRIPTION
                );
                case TELEMETRY_AND_DATA_ANALYSIS -> addModuleIfNotExists(
                        ModuleName.TELEMETRY_AND_DATA_ANALYSIS,
                        "Teaches how to collect and interpret telemetry data to enhance driving performance. Learn how to analyze braking points, throttle application, tire wear, and sector times to make data-driven improvements.",
                        ModuleType.SUBSCRIPTION
                );
                case VEHICLE_SETUP_OPTIMIZATION -> addModuleIfNotExists(
                        ModuleName.VEHICLE_SETUP_OPTIMIZATION,
                        "Explores the impact of car setup adjustments on handling and performance. Learn how to fine-tune suspension, aerodynamics, tire pressures, and gear ratios to suit different tracks and driving styles.",
                        ModuleType.SUBSCRIPTION
                );
                case MENTAL_TOUGHNESS_AND_FOCUS -> addModuleIfNotExists(
                        ModuleName.MENTAL_TOUGHNESS_AND_FOCUS,
                        "Develops the psychological aspects of racing, including concentration, stress management, race-day confidence, and handling pressure. Learn techniques to maintain consistency and stay mentally sharp during intense races.",
                        ModuleType.SUBSCRIPTION
                );
            }
        }

        videoService.initializeModuleVideos();
    }

    public List<Module> getAllModules(UUID ownerId) {
        return moduleRepository.findAllByOwnerId(ownerId);
    }

    public Module getModuleById(UUID moduleId) {
        return moduleRepository.getById(moduleId);
    }
}
