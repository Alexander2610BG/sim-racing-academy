package aleksandarskachkov.simracingacademy.video.service;

import aleksandarskachkov.simracingacademy.exception.DomainException;
import aleksandarskachkov.simracingacademy.track.model.Track;
import aleksandarskachkov.simracingacademy.track.model.TrackName;
import aleksandarskachkov.simracingacademy.track.repository.TrackRepository;
import aleksandarskachkov.simracingacademy.track.service.TrackService;
import aleksandarskachkov.simracingacademy.video.model.Video;
import aleksandarskachkov.simracingacademy.video.repository.VideoRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
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

    public Video createVideo(String title, String videoUrl, String description, TrackName trackName) {
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

    public List<Video> getVideosForTrack(TrackName trackName) {
        return videoRepository.findAllByTrack_Name(trackName);
    }

    @PostConstruct
    public void initializeTrackVideos() {
        createVideo("Bahrain Hot Lap", "https://www.youtube.com/embed/-e4-1y_fDDw?si=_7B6bPvMXnTus2-7", "A fast lap around Imola.", TrackName.BAHRAIN);
        createVideo("Bahrain Track Guide", "https://www.youtube.com/embed/HPxlzAoBVX4", "A detailed guide to mastering Imola.", TrackName.BAHRAIN);

        createVideo("Imola Hot Lap", "https://www.youtube.com/embed/lpNkpVCptIQ", "A fast lap around Imola.", TrackName.IMOLA);
        createVideo("Imola Track Guide", "https://www.youtube.com/embed/HPxlzAoBVX4", "A detailed guide to mastering Imola.", TrackName.IMOLA);

//        https://youtu.be/lpNkpVCptIQ
    }

    // Store video mappings
//    private final Map<TrackName, List<Video>> trackVideos = new HashMap<>();

    // Initialize track video mappings

//    -e4-1y_fDDw&ab
//    @PostConstruct
//    public void initVideos() {
//        trackVideos.put(TrackName.BAHRAIN, List.of(
//                Video.builder()
//                        .title("Sector 1")
//                        .description("Corner 1 - 7")
//                        .videoUrl("https://youtu.be/-e4-1y_fDDw")
//                        .build(),
//
//                Video.builder()
//                        .title("Sector 2")
//                        .description("Corner 8 - 19")
//                        .videoUrl("https://youtu.be/HPxlzAoBVX4")
//                        .build()
//        ));
//
//        trackVideos.put(TrackName.IMOLA, List.of(
//                Video.builder()
//                        .title("Full Lap Guide")
//                        .description("Track Guide for Imola")
//                        .videoUrl("https://youtu.be/ImolaGuide")
//                        .build()
//        ));
//
//        trackVideos.put(TrackName.SUZUKA, List.of(
//                Video.builder()
//                        .title("Suzuka Hot Lap")
//                        .description("Fastest way around Suzuka")
//                        .videoUrl("https://youtu.be/SuzukaLap")
//                        .build()
//        ));
//    }

//    public List<Video> getVideosForTrack(TrackName trackName) {
//        Track track = trackService.getTrackByName(trackName.name());
//
//        return switch (trackName) {
//            case BAHRAIN -> List.of(
//                    Video.builder().title("Sector 1").description("Track guide for Sector 1").track(track).videoUrl("https://youtu.be/-e4-1y_fDDw").build(),
//                    Video.builder().title("Sector 2").description("Track guide for Sector 2").track(track).videoUrl("https://youtu.be/HPxlzAoBVX4").build()
//            );
//
//            case IMOLA -> List.of(
//                    Video.builder().title("Sector 1 & 2").description("Track Guide for sector 1 & 2").track(track).videoUrl("https://youtu.be/ImolaGuide").build(),
//                    Video.builder().title("Sector 3").description("Track Guide for sector 3").track(track).videoUrl("https://youtu.be/ImolaGuide").build()
//            );
//
//            case SUZUKA -> List.of(
//                    Video.builder().title("Sector 1 & 2").description("Track Guide for sector 1 & 2").track(track).videoUrl("https://youtu.be/ImolaGuide").build(),
//                    Video.builder().title("Sector 3").description("Track Guide for sector 3").track(track).videoUrl("https://youtu.be/ImolaGuide").build()
//            );
//
//            case SPA_FRANCORCHAMPS -> List.of(
//                    Video.builder().title("Sector 1 & 2").description("Track Guide for sector 1 & 2").track(track).videoUrl("https://youtu.be/ImolaGuide").build(),
//                    Video.builder().title("Sector 3").description("Track Guide for sector 3").track(track).videoUrl("https://youtu.be/ImolaGuide").build()
//            );
//
//            case MONACO -> List.of(
//                    Video.builder().title("Sector 1 & 2").description("Track Guide for sector 1 & 2").track(track).videoUrl("https://youtu.be/ImolaGuide").build(),
//                    Video.builder().title("Sector 3").description("Track Guide for sector 3").track(track).videoUrl("https://youtu.be/ImolaGuide").build()
//            );
//
//            case MONZA -> List.of(
//                    Video.builder().title("Sector 1 & 2").description("Track Guide for sector 1 & 2").track(track).videoUrl("https://youtu.be/ImolaGuide").build(),
//                    Video.builder().title("Sector 3").description("Track Guide for sector 3").track(track).videoUrl("https://youtu.be/ImolaGuide").build()
//            );
//        };
//    }
}
