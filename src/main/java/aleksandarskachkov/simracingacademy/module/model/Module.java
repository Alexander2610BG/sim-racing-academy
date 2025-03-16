package aleksandarskachkov.simracingacademy.module.model;

import aleksandarskachkov.simracingacademy.subscription.model.Subscription;
import aleksandarskachkov.simracingacademy.user.model.User;
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

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ModuleType type;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "module")
    private List<Video> videos = new ArrayList<>();


//    @ManyToMany(mappedBy = "modules")
//    private List<Subscription> subscriptions = new ArrayList<>();

//    @ManyToOne
//    private User owner;
}
