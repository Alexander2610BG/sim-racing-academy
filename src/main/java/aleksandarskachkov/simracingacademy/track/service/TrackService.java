package aleksandarskachkov.simracingacademy.track.service;

import aleksandarskachkov.simracingacademy.track.model.Track;
import aleksandarskachkov.simracingacademy.track.model.TrackName;
import aleksandarskachkov.simracingacademy.track.model.TrackType;
import aleksandarskachkov.simracingacademy.track.repository.TrackRepository;
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


    public void addTrackIfNotExists(TrackName trackName, String description, String imageUrl, TrackType type) {
        // Check if track already exists
        Optional<Track> existingTrack = Optional.ofNullable(trackRepository.findByName(trackName));
        if (existingTrack.isPresent()) {
            log.info("Track '{}' already exists. Skipping...", trackName);
        } else {
            // Create the track if it does not exist
            createTrack(trackName, description, imageUrl, type, List.of());
        }
    }

    @PostConstruct
    public void initializeTracks() {
        // Define all available tracks
        List<TrackName> trackNames = List.of(
                TrackName.BAHRAIN,
                TrackName.IMOLA,
                TrackName.SUZUKA,
                TrackName.SPA_FRANCORCHAMPS,
                TrackName.MONACO,
                TrackName.MONZA
//                TrackName.SPAIN // Add Spain track here for example if we add new track
        );

        for (TrackName trackName : trackNames) {
            // Add track if not exists
            switch (trackName) {
                case BAHRAIN -> addTrackIfNotExists(
                        TrackName.BAHRAIN,
                        "Bahrain International Circuit",
                        "https://www.formula1.com/content/dam/fom-website/2018-redesign-assets/Circuit%20maps%2016x9/Bahrain_Circuit.png",
                        TrackType.DEFAULT
                );
                case IMOLA -> addTrackIfNotExists(
                        TrackName.IMOLA,
                        "Autodromo Enzo e Dino Ferrari",
                        "https://media.formula1.com/image/upload/f_auto,c_limit,q_auto,w_1320/content/dam/fom-website/2018-redesign-assets/Circuit%20maps%2016x9/Emilia_Romagna_Circuit",
                        TrackType.DEFAULT
                );
                case SUZUKA -> addTrackIfNotExists(
                        TrackName.SUZUKA,
                        "Suzuka International Racing Course",
                        "https://media.formula1.com/image/upload/f_auto,c_limit,q_auto,w_1320/content/dam/fom-website/2018-redesign-assets/Circuit%20maps%2016x9/Japan_Circuit",
                        TrackType.SUBSCRIPTION
                );
                case SPA_FRANCORCHAMPS -> addTrackIfNotExists(
                        TrackName.SPA_FRANCORCHAMPS,
                        "Circuit de Spa-Francorchamps",
                        "https://www.formula1.com/content/dam/fom-website/2018-redesign-assets/Circuit%20maps%2016x9/Belgium_Circuit.png",
                        TrackType.SUBSCRIPTION
                );
                case MONACO -> addTrackIfNotExists(
                        TrackName.MONACO,
                        "Circuit de Monaco",
                        "https://media.formula1.com/image/upload/f_auto,c_limit,q_auto,w_1320/content/dam/fom-website/2018-redesign-assets/Circuit%20maps%2016x9/Monaco_Circuit",
                        TrackType.SUBSCRIPTION
                );
                case MONZA -> addTrackIfNotExists(
                        TrackName.MONZA,
                        "Monza National Autodrome",
                        "https://www.formula1.com/content/dam/fom-website/2018-redesign-assets/Circuit%20maps%2016x9/Italy_Circuit.png",
                        TrackType.SUBSCRIPTION
                );
//                case SPAIN -> addTrackIfNotExists(
//                        TrackName.SPAIN,
//                        "Circuit de Spain",
//                        "https://www.formula1.com/content/dam/fom-website/2018-redesign-assets/Circuit%20maps%2016x9/Spain_Circuit.png",
//                        TrackType.DEFAULT
//                );
            }
        }

        // Initialize videos for the tracks after tracks are added
        videoService.initializeTrackVideos();
}

    public List<Track> getAllTracks(UUID ownerId) {

    return trackRepository.findAllTracksByUserId(ownerId);
    }

    public List<Track> getAllTracksByType(TrackType type) {
        return trackRepository.findAllByType(type);
    }

    public Track getTrackById(UUID trackId) {
        return trackRepository.getById(trackId);
    }
}
