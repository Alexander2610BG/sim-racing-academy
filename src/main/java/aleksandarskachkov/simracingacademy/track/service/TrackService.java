package aleksandarskachkov.simracingacademy.track.service;

import aleksandarskachkov.simracingacademy.track.model.Track;
import aleksandarskachkov.simracingacademy.track.model.TrackName;
import aleksandarskachkov.simracingacademy.track.model.TrackType;
import aleksandarskachkov.simracingacademy.track.repository.TrackRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
public class TrackService {

    private final TrackRepository trackRepository;

    @Autowired
    public TrackService(TrackRepository trackRepository) {
        this.trackRepository = trackRepository;
    }

    public void createDefaultTracks() {

        Track bahrain = initializeTrack(TrackName.BAHRAIN, "bahrain", new BigDecimal("0.00"), TrackType.DEFAULT);
        Track imola = initializeTrack(TrackName.IMOLA, "imola", new BigDecimal("0.00"), TrackType.DEFAULT);

        trackRepository.save(bahrain);
        trackRepository.save(imola);
    }

    private Track initializeTrack(TrackName name, String description, BigDecimal price, TrackType type) {


        return Track.builder()
                .name(name)
                .description(description)
                .price(price)
                .type(type)
                .build();
    }
}
