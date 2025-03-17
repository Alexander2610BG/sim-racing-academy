package aleksandarskachkov.simracingacademy.video.service;

import aleksandarskachkov.simracingacademy.exception.DomainException;
import aleksandarskachkov.simracingacademy.module.model.Module;
import aleksandarskachkov.simracingacademy.module.model.ModuleName;
import aleksandarskachkov.simracingacademy.module.repository.ModuleRepository;
import aleksandarskachkov.simracingacademy.track.model.Track;
import aleksandarskachkov.simracingacademy.track.model.TrackName;
import aleksandarskachkov.simracingacademy.track.repository.TrackRepository;
import aleksandarskachkov.simracingacademy.video.model.Video;
import aleksandarskachkov.simracingacademy.video.repository.VideoRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class VideoService {


    private final VideoRepository videoRepository;
    private final TrackRepository trackRepository;
    private final ModuleRepository moduleRepository;

    @Autowired
    public VideoService(VideoRepository videoRepository, TrackRepository trackRepository, ModuleRepository moduleRepository) {
        this.videoRepository = videoRepository;
        this.trackRepository = trackRepository;
        this.moduleRepository = moduleRepository;
    }

    public Video createVideoForTrack(String title, String videoUrl, String description, TrackName trackName) {
      Optional<Track> trackOptional = Optional.ofNullable(trackRepository.findByName(trackName));
      if (trackOptional.isEmpty()) {
          throw new DomainException("Track not found: " + trackName);
      }

        Video video = Video.builder()
                .title(title)
                .videoUrl(videoUrl)
                .description(description)
                .track(trackOptional.get())
                .build();

        return videoRepository.save(video);
    }

    public Video createVideoForModule(String title, String videoUrl, String description, ModuleName moduleName) {
        Optional<Module> moduleOptional = Optional.ofNullable(moduleRepository.findByName(moduleName));
        if (moduleOptional.isEmpty()) {
            throw new DomainException("Module not found: " + moduleName);
        }

        Video video = Video.builder()
                .title(title)
                .videoUrl(videoUrl)
                .description(description)
                .module(moduleOptional.get())
                .build();

        return videoRepository.save(video);
    }

    public List<Video> getVideosForTrack(UUID trackId) {
        return videoRepository.findAllByTrackId(trackId);
    }

    private void addVideoIfNotExistsForTrack(String title, String url, String description, TrackName trackName) {
        if (!videoRepository.existsByTitleAndTrackName(title, trackName)) {
            createVideoForTrack(title, url, description, trackName);
            log.info("Added video for track: %s with title: %s".formatted(trackName, title));
        } else {
            log.info("Video already exists for track: %s with title: %s".formatted(trackName, title));
        }
    }

    private void addVideoIfNotExistsForModule(String title, String url, String description, ModuleName moduleName) {
        if (!videoRepository.existsByTitleAndModuleName(title, moduleName)) {
            createVideoForModule(title, url, description, moduleName);
            log.info("Added video for module: %s with title: %s".formatted(moduleName, title));
        } else {
            log.info("Video already exists for module: %s with title: %s".formatted(moduleName, title));
        }
    }

    @PostConstruct
    public void initializeTrackVideos() {
        List<Track> tracks = trackRepository.findAll();

        for (Track track : tracks) {
            TrackName trackName = track.getName();

            // Add videos for this track if they don't already exist
            switch (trackName) {
                case BAHRAIN -> {
                    addVideoIfNotExistsForTrack("Bahrain Hot Lap", "https://www.youtube.com/embed/PC6XRaLGUdc", "A fast lap around Bahrain.", trackName);
                    addVideoIfNotExistsForTrack("Bahrain Track Guide", "https://www.youtube.com/embed/-e4-1y_fDDw", "A detailed guide to mastering Bahrain.", trackName);
                }
                case IMOLA -> {
                    addVideoIfNotExistsForTrack("Imola Hot Lap", "https://www.youtube.com/embed/oPGG-ip5IaQ", "A fast lap around Imola.", trackName);
                    addVideoIfNotExistsForTrack("Imola Track Guide", "https://www.youtube.com/embed/lpNkpVCptIQ", "A detailed guide to mastering Imola.", trackName);
                }
                case SUZUKA -> {
                    addVideoIfNotExistsForTrack("Suzuka Hot Lap", "https://www.youtube.com/embed/q-85hyxBlvc", "A fast lap around Suzuka.", trackName);
                    addVideoIfNotExistsForTrack("Suzuka Track Guide", "https://www.youtube.com/embed/XUJlIWnyP_0", "A detailed guide to mastering Suzuka.", trackName);
                }
                case SPA_FRANCORCHAMPS -> {
                    addVideoIfNotExistsForTrack("Spa Hot Lap", "https://www.youtube.com/embed/8_0iCbtBJGw", "A fast lap around Spa.", trackName);
                    addVideoIfNotExistsForTrack("Spa Track Guide", "https://www.youtube.com/embed/PMhBVq2QpJU", "A detailed guide to mastering Spa.", trackName);
                }
                case MONACO -> {
                    addVideoIfNotExistsForTrack("Monaco Hot Lap", "https://www.youtube.com/embed/ntZMFR1-Z2E", "A fast lap around Monaco.", trackName);
                    addVideoIfNotExistsForTrack("Monaco Track Guide", "https://www.youtube.com/embed/FL5RiPGxgd0", "A detailed guide to mastering Monaco.", trackName);
                }
                case MONZA -> {
                    addVideoIfNotExistsForTrack("Monza Hot Lap", "https://www.youtube.com/embed/NyLHgXyU4Iw", "A fast lap around Monza.", trackName);
                    addVideoIfNotExistsForTrack("Monza Track Guide", "https://www.youtube.com/embed/Xbhi2Vnk36Q", "A detailed guide to mastering Monza.", trackName);
                }
                case SPAIN -> {
                    addVideoIfNotExistsForTrack("Spain Hot Lap", "https://www.youtube.com/embed/example1", "A fast lap around Spain.", trackName);
                    addVideoIfNotExistsForTrack("Spain Track Guide", "https://www.youtube.com/embed/example2", "A detailed guide to mastering Spain.", trackName);
                }
            }
        }
    }

    @PostConstruct
    public void initializeModuleVideos() {
        List<Module> modules = moduleRepository.findAll();

        for (Module module : modules) {
            ModuleName moduleName = module.getName();

            switch (moduleName) {
                case FOUNDATIONS_OF_SIM_RACING -> {
                    addVideoIfNotExistsForModule("Introduction to Racing Lines", "https://www.youtube.com/embed/VEJh4lLCzRc", "Learn how to find the optimal racing line for better lap times.", moduleName);
                    addVideoIfNotExistsForModule("Braking Techniques Explained", "https://www.youtube.com/embed/I5ctwZ06UBo", "Master the art of braking, including threshold and trail braking.", moduleName);
                    addVideoIfNotExistsForModule("Throttle Control for Smooth Driving", "https://www.youtube.com/embed/YQV3zZuCtkw", "Improve acceleration and corner exits with proper throttle modulation.", moduleName);
                    addVideoIfNotExistsForModule("Understanding Car Control", "https://www.youtube.com/embed/zpasEmXy0Ec", "A guide to steering inputs, weight transfer, and maintaining control at high speeds.", moduleName);
                    addVideoIfNotExistsForModule("Sim Racing Beginner Mistakes", "https://www.youtube.com/embed/JPulXk0orE8", "Avoid common mistakes that slow down new sim racers and improve consistency.", moduleName);
                }
                case ADVANCED_DRIVING_TECHNIQUES -> {
                    addVideoIfNotExistsForModule("Mastering Trail Braking", "https://www.youtube.com/embed/RK6kE0ftFA0", "Learn how to use trail braking to carry more speed into corners.", moduleName);
                    addVideoIfNotExistsForModule("Throttle Modulation for Maximum Grip", "https://www.youtube.com/embed/Wy9dKlXHYOk", "Improve car stability and acceleration with precise throttle control.", moduleName);
                    addVideoIfNotExistsForModule("Weight Transfer and Car Balance", "https://www.youtube.com/embed/kVslPeT6Tt4", "Understand how weight shifts affect grip and handling in different scenarios.", moduleName);
                    addVideoIfNotExistsForModule("Cornering Techniques for Faster Laps", "https://www.youtube.com/embed/KBXMan0Dafw", "Optimize your entry, apex, and exit strategies for smoother and faster cornering.", moduleName);
                    addVideoIfNotExistsForModule("Heel-and-Toe Downshifting Explained", "https://www.youtube.com/embed/xeoLRWCNGcA", "Master this advanced technique for smoother gear changes and better control.", moduleName);
                    addVideoIfNotExistsForModule("Recovering from Oversteer and Understeer", "https://www.youtube.com/embed/EwmDdMzzDjY", "Learn how to react and correct when your car loses grip in different situations.", moduleName);
                }
                case RACECRAFT_AND_STRATEGY -> {
                    addVideoIfNotExistsForModule("Overtaking Strategies in Sim Racing", "https://www.youtube.com/embed/pmsfdv5cF9Y", "Learn how to set up and execute clean and effective overtakes.", moduleName);
                    addVideoIfNotExistsForModule("Defensive Driving Techniques", "https://www.youtube.com/embed/adDKujvgVeQ", "Master positioning and blocking to protect your position without contact.", moduleName);
                    addVideoIfNotExistsForModule("Race Starts and First Lap Survival", "https://www.youtube.com/embed/XlxPb0oJMx0", "Tips for getting the best start and avoiding incidents in turn one.", moduleName);
                    addVideoIfNotExistsForModule("Reading Opponents and Adapting Strategies", "https://www.youtube.com/embed/KyjvSnmynjA", "Understand driver behavior and adjust your race approach accordingly.", moduleName);
                }
                case TELEMETRY_AND_DATA_ANALYSIS -> {
                    addVideoIfNotExistsForModule("Introduction to Telemetry in Sim Racing", "https://www.youtube.com/embed/01xiXDik6rg", "Learn the basics of telemetry and how it can improve your lap times.", moduleName);
                    addVideoIfNotExistsForModule("Analyzing Braking and Throttle Data", "https://www.youtube.com/embed/GS1_nd41Dns", "Understand how to optimize braking points and throttle application for better control.", moduleName);
                    addVideoIfNotExistsForModule("Using Sector Times to Improve Consistency", "https://www.youtube.com/embed/LJvohZ80Skc", "Break down lap data to find where you're gaining or losing time.", moduleName);
                }
                case VEHICLE_SETUP_OPTIMIZATION -> {
                    addVideoIfNotExistsForModule("Understanding Suspension Setup", "https://www.youtube.com/embed/6QE2S7jGcgg", "Learn how adjusting springs, dampers, and ride height affects car handling.", moduleName);
                    addVideoIfNotExistsForModule("Aerodynamics and Downforce Tuning", "https://www.youtube.com/embed/yRTUxxryN2w", "Optimize your wing angles and aero balance for maximum grip and speed.", moduleName);
                    addVideoIfNotExistsForModule("Tire Pressures and Temperature Management", "https://www.youtube.com/embed/Oc6QQVDx2Io", "Discover how tire pressure adjustments impact grip, wear, and stability.", moduleName);
                }
                case MENTAL_TOUGHNESS_AND_FOCUS -> {
                    addVideoIfNotExistsForModule("Building Race-Day Confidence", "https://www.youtube.com/embed/BRhBNSRHpYw", "Learn techniques to stay calm, focused, and confident before and during races.", moduleName);
                    addVideoIfNotExistsForModule("Managing Stress and Pressure in Racing", "https://www.youtube.com/embed/z8aHsiBYArY", "Discover strategies to control nerves, stay composed, and perform under pressure.", moduleName);
                }
            }
        }
    }

    public List<Video> getVideosForModule(UUID moduleId) {
        return videoRepository.findAllByModuleId(moduleId);
    }


//    @PostConstruct
//    public void initializeTrackVideos() {
//        List<Track> tracks = trackRepository.findAll();
//
//        for (Track track : tracks) {
//            TrackName trackName = track.getName();
//
//            // Check if there are already videos for this track
//            if (videoRepository.existsByTrackName(trackName)) {
//                continue; // Skip if videos for this track already exist
//            }
//
//            switch (trackName) {
//                case BAHRAIN -> {
//                    createVideoForTrack("Bahrain Hot Lap", "https://www.youtube.com/embed/PC6XRaLGUdc", "A fast lap around Bahrain.", trackName);
//                    createVideoForTrack("Bahrain Track Guide", "https://www.youtube.com/embed/-e4-1y_fDDw", "A detailed guide to mastering Bahrain.", trackName);
//                }
//                case IMOLA -> {
//                    createVideoForTrack("Imola Hot Lap", "https://www.youtube.com/embed/oPGG-ip5IaQ", "A fast lap around Imola.", trackName);
//                    createVideoForTrack("Imola Track Guide", "https://www.youtube.com/embed/lpNkpVCptIQ", "A detailed guide to mastering Imola.", trackName);
//                }
//                case SUZUKA -> {
//                    createVideoForTrack("Suzuka Hot Lap", "https://www.youtube.com/embed/q-85hyxBlvc", "A fast lap around Suzuka.", trackName);
//                    createVideoForTrack("Suzuka Track Guide", "https://www.youtube.com/embed/XUJlIWnyP_0", "A detailed guide to mastering Suzuka.", trackName);
//                }
//                case SPA_FRANCORCHAMPS -> {
//                    createVideoForTrack("Spa Hot Lap", "https://www.youtube.com/embed/8_0iCbtBJGw", "A fast lap around Spa.", trackName);
//                    createVideoForTrack("Spa Track Guide", "https://www.youtube.com/embed/PMhBVq2QpJU", "A detailed guide to mastering Spa.", trackName);
//                }
//                case MONACO -> {
//                    createVideoForTrack("Monaco Hot Lap", "https://www.youtube.com/embed/ntZMFR1-Z2E", "A fast lap around Monaco.", trackName);
//                    createVideoForTrack("Monaco Track Guide", "https://www.youtube.com/embed/FL5RiPGxgd0", "A detailed guide to mastering Monaco.", trackName);
//                }
//                case MONZA -> {
//                    createVideoForTrack("Monza Hot Lap", "https://www.youtube.com/embed/NyLHgXyU4Iw", "A fast lap around Monza.", trackName);
//                    createVideoForTrack("Monza Track Guide", "https://www.youtube.com/embed/Xbhi2Vnk36Q", "A detailed guide to mastering Monza.", trackName);
//                }
//                case SPAIN -> {
//                    createVideoForTrack("Spain Hot Lap", "https://www.youtube.com/embed/example1", "A fast lap around Spain.", trackName);
//                    createVideoForTrack("Spain Track Guide", "https://www.youtube.com/embed/example2", "A detailed guide to mastering Spain.", trackName);
//                }
//
//
//            }
//        }
//    }
//
//    private void addVideoIfNotExists(String title, String url, String description, TrackName trackName) {
//        if (!videoRepository.existsByTitleAndTrackName(title, trackName)) {
//            createVideoForTrack(title, url, description, trackName);
//        }
//    }

//@PostConstruct
//public void initializeTrackVideos() {
//
    ////         if there are already created videos, itnos making them again
//    List<TrackName> trackNames = List.of(
//            TrackName.BAHRAIN,
//            TrackName.IMOLA,
//            TrackName.SUZUKA,
//            TrackName.SPA_FRANCORCHAMPS,
//            TrackName.MONACO,
//            TrackName.MONZA
//    );
//
//    for (TrackName trackName : trackNames) {
//        Optional<Video> existingVideo = Optional.ofNullable(videoRepository.findByTitle())
//    }
//
//    List<Track> tracks = trackRepository.findAll();
//
//    for (Track track : tracks) {

//        TrackName trackName = track.getName();
//
//
//        switch (trackName) {
//            case BAHRAIN -> {
//                createVideo("Bahrain Hot Lap", "https://www.youtube.com/embed/-e4-1y_fDDw", "A fast lap around Bahrain.", trackName);
//                createVideo("Bahrain Track Guide", "https://www.youtube.com/embed/HPxlzAoBVX4", "A detailed guide to mastering Bahrain.", trackName);
//            }
//            case IMOLA -> {
//                createVideo("Imola Hot Lap", "https://www.youtube.com/embed/oPGG-ip5IaQ", "A fast lap around Imola.", trackName);
//                createVideo("Imola Track Guide", "https://www.youtube.com/embed/lpNkpVCptIQ", "A detailed guide to mastering Imola.", trackName);
//            }
//            case SUZUKA -> {
//                createVideo("Suzuka Hot Lap", "https://www.youtube.com/embed/q-85hyxBlvc", "A fast lap around Suzuka.", trackName);
//                createVideo("Suzuka Track Guide", "https://www.youtube.com/embed/XUJlIWnyP_0", "A detailed guide to mastering Suzuka.", trackName);
//            }
//            case SPA_FRANCORCHAMPS -> {
//                createVideo("Spa Hot Lap", "https://www.youtube.com/embed/8_0iCbtBJGw", "A fast lap around Spa.", trackName);
//                createVideo("Spa Track Guide", "https://www.youtube.com/embed/PMhBVq2QpJU", "A detailed guide to mastering Spa.", trackName);
//            }
//            case MONACO -> {
//                createVideo("Monaco Hot Lap", "https://www.youtube.com/embed/ntZMFR1-Z2E", "A fast lap around Monaco.", trackName);
//                createVideo("Monaco Track Guide", "https://www.youtube.com/embed/FL5RiPGxgd0", "A detailed guide to mastering Monaco.", trackName);
//            }
//            case MONZA -> {
//                createVideo("Monza Hot Lap", "https://www.youtube.com/embed/NyLHgXyU4Iw", "A fast lap around Monza.", trackName);
//                createVideo("Monza Track Guide", "https://www.youtube.com/embed/Xbhi2Vnk36Q", "A detailed guide to mastering Monza.", trackName);
//            }
//        }
//    }
//}

}
