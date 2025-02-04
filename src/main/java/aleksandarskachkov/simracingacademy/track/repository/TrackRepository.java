package aleksandarskachkov.simracingacademy.track.repository;

import aleksandarskachkov.simracingacademy.track.model.Track;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TrackRepository extends JpaRepository<Track, UUID> {
}
