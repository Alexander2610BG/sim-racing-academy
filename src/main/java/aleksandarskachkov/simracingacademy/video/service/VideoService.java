package aleksandarskachkov.simracingacademy.video.service;

import aleksandarskachkov.simracingacademy.exception.DomainException;
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

    @Autowired
    public VideoService(VideoRepository videoRepository, TrackRepository trackRepository) {
        this.videoRepository = videoRepository;
        this.trackRepository = trackRepository;
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

    public List<Video> getVideosForTrack(UUID trackId) {
        return videoRepository.findAllByTrackId(trackId);
    }

    private void addVideoIfNotExists(String title, String url, String description, TrackName trackName) {
        if (!videoRepository.existsByTitleAndTrackName(title, trackName)) {
            createVideoForTrack(title, url, description, trackName);
            log.info("Added video for track: %s with title: %s".formatted(trackName, title));
        } else {
            log.info("Video already exists for track: %s with title: %s".formatted(trackName, title));
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
                    addVideoIfNotExists("Bahrain Hot Lap", "https://www.youtube.com/embed/PC6XRaLGUdc", "A fast lap around Bahrain.", trackName);
                    addVideoIfNotExists("Bahrain Track Guide", "https://www.youtube.com/embed/-e4-1y_fDDw", "A detailed guide to mastering Bahrain.", trackName);
                }
                case IMOLA -> {
                    addVideoIfNotExists("Imola Hot Lap", "https://www.youtube.com/embed/oPGG-ip5IaQ", "A fast lap around Imola.", trackName);
                    addVideoIfNotExists("Imola Track Guide", "https://www.youtube.com/embed/lpNkpVCptIQ", "A detailed guide to mastering Imola.", trackName);
                }
                case SUZUKA -> {
                    addVideoIfNotExists("Suzuka Hot Lap", "https://www.youtube.com/embed/q-85hyxBlvc", "A fast lap around Suzuka.", trackName);
                    addVideoIfNotExists("Suzuka Track Guide", "https://www.youtube.com/embed/XUJlIWnyP_0", "A detailed guide to mastering Suzuka.", trackName);
                }
                case SPA_FRANCORCHAMPS -> {
                    addVideoIfNotExists("Spa Hot Lap", "https://www.youtube.com/embed/8_0iCbtBJGw", "A fast lap around Spa.", trackName);
                    addVideoIfNotExists("Spa Track Guide", "https://www.youtube.com/embed/PMhBVq2QpJU", "A detailed guide to mastering Spa.", trackName);
                }
                case MONACO -> {
                    addVideoIfNotExists("Monaco Hot Lap", "https://www.youtube.com/embed/ntZMFR1-Z2E", "A fast lap around Monaco.", trackName);
                    addVideoIfNotExists("Monaco Track Guide", "https://www.youtube.com/embed/FL5RiPGxgd0", "A detailed guide to mastering Monaco.", trackName);
                }
                case MONZA -> {
                    addVideoIfNotExists("Monza Hot Lap", "https://www.youtube.com/embed/NyLHgXyU4Iw", "A fast lap around Monza.", trackName);
                    addVideoIfNotExists("Monza Track Guide", "https://www.youtube.com/embed/Xbhi2Vnk36Q", "A detailed guide to mastering Monza.", trackName);
                }
                case SPAIN -> {
                    addVideoIfNotExists("Spain Hot Lap", "https://www.youtube.com/embed/example1", "A fast lap around Spain.", trackName);
                    addVideoIfNotExists("Spain Track Guide", "https://www.youtube.com/embed/example2", "A detailed guide to mastering Spain.", trackName);
                }
            }
        }
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
