package aleksandarskachkov.simracingacademy.track.model;

import aleksandarskachkov.simracingacademy.subscription.model.Subscription;
import aleksandarskachkov.simracingacademy.user.model.User;
import aleksandarskachkov.simracingacademy.video.model.Video;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Track {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TrackName name;

    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    private String imageUrl;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TrackType type;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "track")
    private List<Video> videos = new ArrayList<>();

    @ManyToMany(mappedBy = "tracks")
    private List<Subscription> subscriptions = new ArrayList<>();

    //eventually to be removed
//    @ManyToMany(mappedBy = "tracks")
//    private List<User> users = new ArrayList<>();

}
