package aleksandarskachkov.simracingacademy.subscription.service;

import aleksandarskachkov.simracingacademy.subscription.model.Subscription;
import aleksandarskachkov.simracingacademy.subscription.model.SubscriptionPeriod;
import aleksandarskachkov.simracingacademy.subscription.model.SubscriptionStatus;
import aleksandarskachkov.simracingacademy.subscription.model.SubscriptionType;
import aleksandarskachkov.simracingacademy.subscription.repository.SubscriptionRepository;
import aleksandarskachkov.simracingacademy.track.model.Track;
import aleksandarskachkov.simracingacademy.track.model.TrackName;
import aleksandarskachkov.simracingacademy.track.model.TrackType;
import aleksandarskachkov.simracingacademy.track.repository.TrackRepository;
import aleksandarskachkov.simracingacademy.track.service.TrackService;
import aleksandarskachkov.simracingacademy.user.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final TrackService trackService;

    @Autowired
    public SubscriptionService(SubscriptionRepository subscriptionRepository, TrackService trackService) {
        this.subscriptionRepository = subscriptionRepository;
        this.trackService = trackService;
    }

    public Subscription createDefaultSubscription(User user) {

        Subscription subscription = subscriptionRepository.save(initializeSubscription(user));
        log.info("Successfully created new subscription with id [%s] and type [%s]".formatted(subscription.getId(), subscription.getType()));

        return subscription;
    }



    private Subscription initializeSubscription(User user) {

        // to be more safely because the milly seconds
        LocalDateTime now = LocalDateTime.now();

        List<Track> defaultTracks = trackService.createNewDefaultTracks(user);
        user.setTracks(defaultTracks);

        return Subscription.builder()
                .owner(user)
                .status(SubscriptionStatus.ACTIVE)
                .period(SubscriptionPeriod.MONTHLY)
                .type(SubscriptionType.DEFAULT)
                .price(new BigDecimal("0.00"))
                .renewalAllowed(true)
                .createdOn(now)
                .completedOn(now.plusMonths(1))
//                .course(null)
                .tracks(defaultTracks)
                .build();


    }
}
