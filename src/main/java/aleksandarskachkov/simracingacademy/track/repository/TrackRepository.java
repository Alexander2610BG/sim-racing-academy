package aleksandarskachkov.simracingacademy.track.repository;

import aleksandarskachkov.simracingacademy.track.model.Track;
import aleksandarskachkov.simracingacademy.track.model.TrackName;
import aleksandarskachkov.simracingacademy.track.model.TrackType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TrackRepository extends JpaRepository<Track, UUID> {

    List<Track> getAllTracksByType(TrackType type);

    List<Track> findAllByType(TrackType trackType);

    @Query("SELECT t FROM Track t JOIN t.subscriptions s JOIN s.owner u WHERE u.id = :ownerId")
    List<Track> findAllTracksByUserId(@Param("ownerId") UUID ownerId);
}
