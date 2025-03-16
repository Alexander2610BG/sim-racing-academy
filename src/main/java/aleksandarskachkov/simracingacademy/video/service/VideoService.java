package aleksandarskachkov.simracingacademy.video.service;

import aleksandarskachkov.simracingacademy.exception.DomainException;
import aleksandarskachkov.simracingacademy.track.model.Track;
import aleksandarskachkov.simracingacademy.track.model.TrackName;
import aleksandarskachkov.simracingacademy.track.repository.TrackRepository;
import aleksandarskachkov.simracingacademy.track.service.TrackService;
import aleksandarskachkov.simracingacademy.video.model.Video;
import aleksandarskachkov.simracingacademy.video.repository.VideoRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
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

    public List<Video> getVideosForTrack(UUID trackId) {
        return videoRepository.findAllByTrackId(trackId);
    }

    @PostConstruct
    public void initializeTrackVideos() {

        // if there are already created videos, itnos making them again
        if (videoRepository.count() > 0) {
            return;
        }

        List<Track> tracks = trackRepository.findAll();

        for (Track track : tracks) {
            TrackName trackName = track.getName();

            switch (trackName) {
                case BAHRAIN -> {
                    createVideo("Bahrain Hot Lap", "https://www.youtube.com/embed/-e4-1y_fDDw", "A fast lap around Bahrain.", trackName);
                    createVideo("Bahrain Track Guide", "https://www.youtube.com/embed/HPxlzAoBVX4", "A detailed guide to mastering Bahrain.", trackName);
                }
                case IMOLA -> {
                    createVideo("Imola Hot Lap", "https://www.youtube.com/embed/oPGG-ip5IaQ", "A fast lap around Imola.", trackName);
                    createVideo("Imola Track Guide", "https://www.youtube.com/embed/lpNkpVCptIQ", "A detailed guide to mastering Imola.", trackName);
                }
                case SUZUKA -> {
                    createVideo("Suzuka Hot Lap", "https://www.youtube.com/embed/q-85hyxBlvc", "A fast lap around Suzuka.", trackName);
                    createVideo("Suzuka Track Guide", "https://www.youtube.com/embed/XUJlIWnyP_0", "A detailed guide to mastering Suzuka.", trackName);
                }
                case SPA_FRANCORCHAMPS -> {
                    createVideo("Spa Hot Lap", "https://www.youtube.com/embed/8_0iCbtBJGw", "A fast lap around Spa.", trackName);
                    createVideo("Spa Track Guide", "https://www.youtube.com/embed/PMhBVq2QpJU", "A detailed guide to mastering Spa.", trackName);
                }
                case MONACO -> {
                    createVideo("Monaco Hot Lap", "https://www.youtube.com/embed/ntZMFR1-Z2E", "A fast lap around Monaco.", trackName);
                    createVideo("Monaco Track Guide", "https://www.youtube.com/embed/FL5RiPGxgd0", "A detailed guide to mastering Monaco.", trackName);
                }
                case MONZA -> {
                    createVideo("Monza Hot Lap", "https://www.youtube.com/embed/NyLHgXyU4Iw", "A fast lap around Monza.", trackName);
                    createVideo("Monza Track Guide", "https://www.youtube.com/embed/Xbhi2Vnk36Q", "A detailed guide to mastering Monza.", trackName);
                }
            }
        }
    }

}
