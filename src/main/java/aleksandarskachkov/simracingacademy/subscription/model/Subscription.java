package aleksandarskachkov.simracingacademy.subscription.model;

import aleksandarskachkov.simracingacademy.module.model.Module;
import aleksandarskachkov.simracingacademy.track.model.Track;
import aleksandarskachkov.simracingacademy.user.model.User;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    private User owner;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SubscriptionStatus status;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SubscriptionPeriod period;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SubscriptionType type;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private boolean renewalAllowed;

    @Column(nullable = false)
    private LocalDateTime createdOn;

    @Column(nullable = false)
    private LocalDateTime completedOn;

    @ManyToMany
    @JoinTable(
            joinColumns = @JoinColumn,
            inverseJoinColumns = @JoinColumn
    )
    private List<Track> tracks = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            joinColumns = @JoinColumn,
            inverseJoinColumns = @JoinColumn
    )
    private List<Module> modules = new ArrayList<>();

//    @ManyToMany
//    private List<Module> modules = new ArrayList<>();
}
