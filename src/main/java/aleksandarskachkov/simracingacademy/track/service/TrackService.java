package aleksandarskachkov.simracingacademy.track.service;

import aleksandarskachkov.simracingacademy.track.model.Track;
import aleksandarskachkov.simracingacademy.track.model.TrackName;
import aleksandarskachkov.simracingacademy.track.model.TrackType;
import aleksandarskachkov.simracingacademy.track.repository.TrackRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
public class TrackService {

    private final TrackRepository trackRepository;

    @Autowired
    public TrackService(TrackRepository trackRepository) {
        this.trackRepository = trackRepository;
    }

    public void createDefaultTracks() {

        Track bahrain = createTrack(TrackName.BAHRAIN, "bahrain", new BigDecimal("0.00"), TrackType.DEFAULT);
        Track imola = createTrack(TrackName.IMOLA, "imola", new BigDecimal("0.00"), TrackType.DEFAULT);

    }

    public List<Track> getDefaultTracks() {
        return trackRepository.findAllByType(TrackType.DEFAULT);
    }

    private Track createTrack(TrackName name, String description, BigDecimal price, TrackType type) {


        Track track = Track.builder()
                .name(name)
                .description(description)
                .price(price)
                .type(type)
                .build();

        return trackRepository.save(track);
    }
}
