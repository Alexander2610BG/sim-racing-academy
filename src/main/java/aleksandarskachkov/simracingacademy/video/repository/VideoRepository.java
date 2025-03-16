package aleksandarskachkov.simracingacademy.video.repository;

import aleksandarskachkov.simracingacademy.track.model.TrackName;
import aleksandarskachkov.simracingacademy.video.model.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface VideoRepository extends JpaRepository<Video, UUID> {
    List<Video> findAllByTrack_Name(TrackName trackName);

    List<Video> findAllByTrackId(UUID trackId);

    boolean existsByTrackName(TrackName trackName);

    Video findByTitle(String title);

    Optional<Video> findByTitleAndTrackId(String title, UUID id);

    boolean existsByTitleAndTrackName(String title, TrackName trackName);
}
