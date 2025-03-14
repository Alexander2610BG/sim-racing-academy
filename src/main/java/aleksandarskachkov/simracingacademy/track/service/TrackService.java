package aleksandarskachkov.simracingacademy.track.service;

import aleksandarskachkov.simracingacademy.exception.DomainException;
import aleksandarskachkov.simracingacademy.track.model.Track;
import aleksandarskachkov.simracingacademy.track.model.TrackName;
import aleksandarskachkov.simracingacademy.track.model.TrackType;
import aleksandarskachkov.simracingacademy.track.repository.TrackRepository;
import aleksandarskachkov.simracingacademy.user.model.User;
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


    public List<Track> getDefaultTracks() {
        return trackRepository.findAllByType(TrackType.DEFAULT);
    }


    private static final List<Track> DEFAULT_TRACKS = List.of(
            Track.builder()
                    .name(TrackName.BAHRAIN)
                    .description("Bahrain International Circuit")
                    .price(new BigDecimal("0.00"))
                    .type(TrackType.DEFAULT)
                    .imageUrl("https://www.formula1.com/content/dam/fom-website/2018-redesign-assets/Circuit%20maps%2016x9/Bahrain_Circuit.png")
//                    .videos(List.of(Video))
                    .build(),

            Track.builder()
                    .name(TrackName.IMOLA)
                    .description("Autodromo Enzo e Dino Ferrari")
                    .price(new BigDecimal("0.00"))
                    .type(TrackType.DEFAULT)
                    .imageUrl("https://media.formula1.com/image/upload/f_auto,c_limit,q_auto,w_1320/content/dam/fom-website/2018-redesign-assets/Circuit%20maps%2016x9/Emilia_Romagna_Circuit")
//                    .videos(List.of(Video))
                    .build()
    );

    private static final List<Track> UPGRADED_TRACKS = List.of(
            Track.builder()
                    .name(TrackName.SUZUKA)
                    .description("Suzuka International Racing Course")
                    .price(new BigDecimal("10.00"))
                    .type(TrackType.SUBSCRIPTION)
                    .imageUrl("https://media.formula1.com/image/upload/f_auto,c_limit,q_auto,w_1320/content/dam/fom-website/2018-redesign-assets/Circuit%20maps%2016x9/Japan_Circuit")
                    .build(),

            Track.builder()
                    .name(TrackName.SPA_FRANCORCHAMPS)
                    .description("Circuit de Spa-Francorchamps")
                    .price(new BigDecimal("10.00"))
                    .type(TrackType.SUBSCRIPTION)
                    .imageUrl("https://www.formula1.com/content/dam/fom-website/2018-redesign-assets/Circuit%20maps%2016x9/Belgium_Circuit.png")
                    .build(),

            Track.builder()
                    .name(TrackName.MONACO)
                    .description("Circuit de Monaco")
                    .price(new BigDecimal("10.00"))
                    .type(TrackType.SUBSCRIPTION)
                    .imageUrl("https://media.formula1.com/image/upload/f_auto,c_limit,q_auto,w_1320/content/dam/fom-website/2018-redesign-assets/Circuit%20maps%2016x9/Monaco_Circuit")
                    .build(),

            Track.builder()
                    .name(TrackName.MONZA)
                    .description("Monza National Autodrome")
                    .price(new BigDecimal("10.00"))
                    .type(TrackType.SUBSCRIPTION)
                    .imageUrl("https://www.formula1.com/content/dam/fom-website/2018-redesign-assets/Circuit%20maps%2016x9/Italy_Circuit.png")
                    .build()

    );

//    private static final

    public List<Track> createNewDefaultTracks(User user) {
        List<Track> savedTracks = DEFAULT_TRACKS.stream()
                .map(t -> {
                    Track newTrack = Track.builder()
                            .name(t.getName())
                            .description(t.getDescription())
                            .price(t.getPrice())
                            .imageUrl(t.getImageUrl())
                            .type(t.getType())
                            .build();

                    return trackRepository.save(newTrack);
                })
                .toList();

        return savedTracks;
    }

    public List<Track> createNewUpgradeTracks(User user) {
        List<Track> savedTracks = UPGRADED_TRACKS.stream()
                .map(t -> {
                    Track newTrack = Track.builder()
                            .name(t.getName())
                            .description(t.getDescription())
                            .price(t.getPrice())
                            .imageUrl(t.getImageUrl())
                            .type(t.getType())
                            .build();

                    return trackRepository.save(newTrack);
                })
                .toList();

        return savedTracks;
    }

    public List<Track> getAllTracks(UUID ownerId) {

//        return trackRepository.findAllTracksByUserId(ownerId);
    return trackRepository.findAllTracksByUserId(ownerId);
    }

    public Track getTrackById(UUID id) {

        return trackRepository.findById(id).orElseThrow(() -> new DomainException("Track with id [%s] does not exits.".formatted(id)));
    }
}
