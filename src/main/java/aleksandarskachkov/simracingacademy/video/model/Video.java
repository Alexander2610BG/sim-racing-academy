package aleksandarskachkov.simracingacademy.video.model;

import aleksandarskachkov.simracingacademy.module.model.Module;
import aleksandarskachkov.simracingacademy.track.model.Track;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String videoUrl;

    private String description;

    // Many to one, because each module and have many videos
    // and one video cannot be in other modules
    @ManyToOne
    private Module module;

    @ManyToOne
    private Track track;
}
