package aleksandarskachkov.simracingacademy.track.service;

import aleksandarskachkov.simracingacademy.exception.DomainException;
import aleksandarskachkov.simracingacademy.track.model.Track;
import aleksandarskachkov.simracingacademy.track.model.TrackName;
import aleksandarskachkov.simracingacademy.track.model.TrackType;
import aleksandarskachkov.simracingacademy.track.repository.TrackRepository;
import aleksandarskachkov.simracingacademy.user.model.User;
import aleksandarskachkov.simracingacademy.video.model.Video;
import aleksandarskachkov.simracingacademy.video.repository.VideoRepository;
//import aleksandarskachkov.simracingacademy.video.service.VideoService;
import aleksandarskachkov.simracingacademy.video.service.VideoService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class TrackService {

    private final TrackRepository trackRepository;
    private final VideoService videoService;

    @Autowired
    public TrackService(TrackRepository trackRepository, VideoService videoService) {
        this.trackRepository = trackRepository;
        this.videoService = videoService;
    }

    public Track createTrack(TrackName trackName, String description, String imageUrl, TrackType type, List<Video> videos) {

        Track track = Track.builder()
                .name(trackName)
                .description(description)
                .imageUrl(imageUrl)
                .type(type)
                .videos(videos)
                .build();

        log.info("Created %s".formatted(trackName));


        return trackRepository.save(track);
    }

    @PostConstruct
    public void initializeTracks() {

        List<TrackName> trackNames = List.of(
                TrackName.BAHRAIN,
                TrackName.IMOLA,
                TrackName.SUZUKA,
                TrackName.SPA_FRANCORCHAMPS,
                TrackName.MONACO,
                TrackName.MONZA
        );
        for (TrackName trackName : trackNames) {
            Optional<Track> existingTrack = Optional.ofNullable(trackRepository.findByName(trackName));

            if (existingTrack.isPresent()) {
                log.info("Track '{}' already exists. Skipping...", trackName);
                continue;
            }


            List<Video> videosForBahrain = videoService.getVideosForTrack(TrackName.BAHRAIN);
            List<Video> videosForImola = videoService.getVideosForTrack(TrackName.IMOLA);
            List<Video> videosForSuzuka = videoService.getVideosForTrack(TrackName.SUZUKA);
            List<Video> videosForSpa = videoService.getVideosForTrack(TrackName.SPA_FRANCORCHAMPS);
            List<Video> videosForMonaco = videoService.getVideosForTrack(TrackName.MONACO);
            List<Video> videosForMonza = videoService.getVideosForTrack(TrackName.MONZA);

            createTrack(
                    TrackName.BAHRAIN,
                    "Bahrain International Circuit",
//                    new BigDecimal(0.00),
                    "https://www.formula1.com/content/dam/fom-website/2018-redesign-assets/Circuit%20maps%2016x9/Bahrain_Circuit.png",
                    TrackType.DEFAULT,
                    videosForBahrain

            );

            createTrack(
                    TrackName.IMOLA,
                    "Autodromo Enzo e Dino Ferrari",
//                    new BigDecimal("0.00"),
                    "https://media.formula1.com/image/upload/f_auto,c_limit,q_auto,w_1320/content/dam/fom-website/2018-redesign-assets/Circuit%20maps%2016x9/Emilia_Romagna_Circuit",
                    TrackType.DEFAULT,
                    videosForImola
            );

            createTrack(
                    TrackName.SUZUKA,
                    "Suzuka International Racing Course",
                    "https://media.formula1.com/image/upload/f_auto,c_limit,q_auto,w_1320/content/dam/fom-website/2018-redesign-assets/Circuit%20maps%2016x9/Japan_Circuit",
                    TrackType.SUBSCRIPTION,
                    videosForSuzuka
            );

            createTrack(
                    TrackName.SPA_FRANCORCHAMPS,
                    "Circuit de Spa-Francorchamps",
                    "https://www.formula1.com/content/dam/fom-website/2018-redesign-assets/Circuit%20maps%2016x9/Belgium_Circuit.png",
                    TrackType.SUBSCRIPTION,
                    videosForSpa
            );

            createTrack(
                    TrackName.MONACO,
                    "Circuit de Monaco",
                    "https://media.formula1.com/image/upload/f_auto,c_limit,q_auto,w_1320/content/dam/fom-website/2018-redesign-assets/Circuit%20maps%2016x9/Monaco_Circuit",
                    TrackType.SUBSCRIPTION,
                    videosForMonaco
            );

            createTrack(
                    TrackName.MONZA,
                    "Monza National Autodrome",
                    "https://www.formula1.com/content/dam/fom-website/2018-redesign-assets/Circuit%20maps%2016x9/Italy_Circuit.png",
                    TrackType.SUBSCRIPTION,
                    videosForMonza
            );
        }

    }



    public List<Track> getAllTracks(UUID ownerId) {

    return trackRepository.findAllTracksByUserId(ownerId);
    }

    public List<Track> getAllTracksByType(TrackType type) {
        return trackRepository.findAllByType(type);
    }
}
