package aleksandarskachkov.simracingacademy.video.repository;

import aleksandarskachkov.simracingacademy.module.model.ModuleName;
import aleksandarskachkov.simracingacademy.track.model.TrackName;
import aleksandarskachkov.simracingacademy.video.model.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface VideoRepository extends JpaRepository<Video, UUID> {

    List<Video> findAllByTrackId(UUID trackId);

    boolean existsByTitleAndTrackName(String title, TrackName trackName);

    boolean existsByTitleAndModuleName(String title, ModuleName moduleName);

    List<Video> findAllByModuleId(UUID moduleId);
}
