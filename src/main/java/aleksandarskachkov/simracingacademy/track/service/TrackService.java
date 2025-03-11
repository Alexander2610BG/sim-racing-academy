package aleksandarskachkov.simracingacademy.track.service;

import aleksandarskachkov.simracingacademy.track.model.Track;
import aleksandarskachkov.simracingacademy.track.model.TrackName;
import aleksandarskachkov.simracingacademy.track.model.TrackType;
import aleksandarskachkov.simracingacademy.track.repository.TrackRepository;
import aleksandarskachkov.simracingacademy.video.model.Video;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class TrackService {

    private final TrackRepository trackRepository;



    @Autowired
    public TrackService(TrackRepository trackRepository) {
        this.trackRepository = trackRepository;
    }

//    public void createDefaultTracks() {
//
//        Track bahrain = createTrack(TrackName.BAHRAIN, "bahrain", new BigDecimal("0.00"), TrackType.DEFAULT);
//        Track imola = createTrack(TrackName.IMOLA, "imola", new BigDecimal("0.00"), TrackType.DEFAULT);
//
//    }

    public List<Track> getDefaultTracks() {
        return trackRepository.findAllByType(TrackType.DEFAULT);
    }

    private static final List<Track> DEFAULT_TRACKS = List.of(
            Track.builder()
                    .name(TrackName.BAHRAIN)
                    .description("Bahrain International Circuit")
                    .price(new BigDecimal("0.00"))
                    .type(TrackType.DEFAULT)
//                    .videos(List.of(Video))
                    .build()
    );

    public void initializeTracks() {
        DEFAULT_TRACKS.forEach(trackRepository::save);
    }

//    private Track createTrack(TrackName name, String description, BigDecimal price, TrackType type) {
//
//
//        Track track = Track.builder()
//                .name(name)
//                .description(description)
//                .price(price)
//                .type(type)
//                .build();
//
//        return trackRepository.save(track);
//    }
}
