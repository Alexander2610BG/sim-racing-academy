package aleksandarskachkov.simracingacademy.module.model;

//import aleksandarskachkov.simracingacademy.course.model.Course;
import aleksandarskachkov.simracingacademy.subscription.model.Subscription;
import aleksandarskachkov.simracingacademy.video.model.Video;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Module {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    private String description;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "module")
    private List<Video> videos = new ArrayList<>();

//    @OneToMany(fetch = FetchType.EAGER)
//    private Subscription subscription;
}
