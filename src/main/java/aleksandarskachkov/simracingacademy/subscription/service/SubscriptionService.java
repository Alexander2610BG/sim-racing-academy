package aleksandarskachkov.simracingacademy.subscription.service;

import aleksandarskachkov.simracingacademy.exception.DomainException;
import aleksandarskachkov.simracingacademy.exception.NoActiveSubscriptionFound;
import aleksandarskachkov.simracingacademy.module.model.Module;
import aleksandarskachkov.simracingacademy.module.model.ModuleType;
import aleksandarskachkov.simracingacademy.module.repository.ModuleRepository;
import aleksandarskachkov.simracingacademy.subscription.model.Subscription;
import aleksandarskachkov.simracingacademy.subscription.model.SubscriptionPeriod;
import aleksandarskachkov.simracingacademy.subscription.model.SubscriptionStatus;
import aleksandarskachkov.simracingacademy.subscription.model.SubscriptionType;
import aleksandarskachkov.simracingacademy.subscription.repository.SubscriptionRepository;
import aleksandarskachkov.simracingacademy.track.model.Track;
import aleksandarskachkov.simracingacademy.track.model.TrackType;
import aleksandarskachkov.simracingacademy.track.service.TrackService;
import aleksandarskachkov.simracingacademy.transaction.model.Transaction;
import aleksandarskachkov.simracingacademy.transaction.model.TransactionStatus;
import aleksandarskachkov.simracingacademy.user.model.User;
import aleksandarskachkov.simracingacademy.wallet.service.WalletService;
import aleksandarskachkov.simracingacademy.web.dto.UpgradeRequest;
import jakarta.transaction.Transactional;
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
    private final WalletService walletService;
    private final ModuleRepository moduleRepository;

    @Autowired
    public SubscriptionService(SubscriptionRepository subscriptionRepository, TrackService trackService, WalletService walletService, ModuleRepository moduleRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.trackService = trackService;
        this.walletService = walletService;
        this.moduleRepository = moduleRepository;
    }

    public Subscription createDefaultSubscription(User user) {

        Subscription subscription = subscriptionRepository.save(initializeSubscription(user));
        log.info("Successfully created new subscription with id [%s] and type [%s]".formatted(subscription.getId(), subscription.getType()));

        return subscription;
    }



    private Subscription initializeSubscription(User user) {

        // to be more safely because the milly seconds
        LocalDateTime now = LocalDateTime.now();

        List<Track> defaultTracks = getDefaultTracks(user);
        List<Module> defaultModules = getDefaultModules(user);

        user.setTracks(defaultTracks);
        user.setModules(defaultModules);

        return Subscription.builder()
                .owner(user)
                .status(SubscriptionStatus.ACTIVE)
                .period(SubscriptionPeriod.MONTHLY)
                .type(SubscriptionType.DEFAULT)
                .price(new BigDecimal("0.00"))
                .renewalAllowed(true)
                .createdOn(now)
                .completedOn(now.plusMonths(1))
                .tracks(defaultTracks)
                .modules(defaultModules)
                .build();
    }

    private List<Track> getDefaultTracks(User user) {

        return trackService.getAllTracksByType(TrackType.DEFAULT);
    }

    private List<Track> getSubscriptionTracks(User user) {

        List<Track> subscriptionTracks = trackService.getAllTracksByType(TrackType.SUBSCRIPTION);
        user.setTracks(subscriptionTracks);
        return subscriptionTracks;
    }

    private List<Module> getDefaultModules(User user) {
        return moduleRepository.getAllByType(ModuleType.DEFAULT);
    }

    private List<Module> getSubscriptionModules(User user) {
        List<Module> subscriptionModules = moduleRepository.getAllByType(ModuleType.SUBSCRIPTION);
        user.setModules(subscriptionModules);
        return subscriptionModules;
    }


    @Transactional
    public Transaction upgrade(User user, SubscriptionType subscriptionType, UpgradeRequest upgradeRequest) {

        Optional<Subscription> optionalSubscription = subscriptionRepository.findByStatusAndOwnerId(SubscriptionStatus.ACTIVE, user.getId());
        if (optionalSubscription.isEmpty()) {
            throw new NoActiveSubscriptionFound("No active subscription has been found for user with id = [%s]".formatted(user.getId()));
        }

        Subscription currentSubscription = optionalSubscription.get();

        SubscriptionPeriod subscriptionPeriod = upgradeRequest.getSubscriptionPeriod();
        String period = subscriptionPeriod.name().substring(0, 1).toUpperCase() + subscriptionPeriod.name().substring(1);
        String type = subscriptionType.name().substring(0, 1).toUpperCase() + subscriptionType.name().substring(1);
        String chargedDescription = "Purchase of %s %s subscription".formatted(period, type);
        BigDecimal subscriptionPrice = getSubscriptionPrice(subscriptionType, subscriptionPeriod);

        Transaction chargeResult = walletService.charge(user, user.getWallet().getId(), subscriptionPrice, chargedDescription);

        if (chargeResult.getStatus() == TransactionStatus.FAILED) {
            log.warn("Charge for subscription failed for user with id [%s], subscription type  [%s]".formatted(user.getId(), subscriptionType));
            return chargeResult;
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime completedOn;
        if (subscriptionPeriod == SubscriptionPeriod.MONTHLY) {
            completedOn = now.plusMonths(1);
        } else {
            completedOn = now.plusYears(1);
        }

        // sets user's track

        List<Track> upgradeTracks = getSubscriptionTracks(user);
        List<Track> defaultTracks = getDefaultTracks(user);


        List<Track> allTracks = new ArrayList<>(defaultTracks);

        if (subscriptionType == SubscriptionType.PREMIUM ||subscriptionType == SubscriptionType.ULTIMATE) {
            allTracks.addAll(upgradeTracks);
        } else {
            user.setTracks(getDefaultTracks(user));
        }

        user.setTracks(allTracks);

        // set's user modules

        List<Module> defaultModules = getDefaultModules(user);
        List<Module> upgradedModules = getSubscriptionModules(user);

        List<Module> allModules = new ArrayList<>(defaultModules);

        if (subscriptionType == SubscriptionType.ULTIMATE) {
            allModules.addAll(upgradedModules);
        } else {
            user.setModules(getDefaultModules(user));
        }

        user.setModules(allModules);

        Subscription newSubscription = Subscription.builder()
                .owner(user)
                .status(SubscriptionStatus.ACTIVE)
                .period(subscriptionPeriod)
                .type(subscriptionType)
                .price(subscriptionPrice)
                .renewalAllowed(subscriptionPeriod == SubscriptionPeriod.MONTHLY)
                .tracks(allTracks)
                .modules(allModules)
                .createdOn(now)
                .completedOn(completedOn)
                .build();


        // stop the current sub
        currentSubscription.setCompletedOn(now);
        currentSubscription.setStatus(SubscriptionStatus.COMPLETED);

        subscriptionRepository.save(currentSubscription);
        subscriptionRepository.save(newSubscription);

        return chargeResult;
    }

    private BigDecimal getSubscriptionPrice(SubscriptionType subscriptionType, SubscriptionPeriod subscriptionPeriod) {

        if (subscriptionType == SubscriptionType.DEFAULT) {
            return BigDecimal.ZERO;
        } else if (subscriptionType == SubscriptionType.PREMIUM && subscriptionPeriod == SubscriptionPeriod.MONTHLY) {
            return new BigDecimal("19.99");
        } else if (subscriptionType == SubscriptionType.PREMIUM && subscriptionPeriod == SubscriptionPeriod.YEARLY) {
            return new BigDecimal("199.99");
        } else if (subscriptionType == SubscriptionType.ULTIMATE && subscriptionPeriod == SubscriptionPeriod.MONTHLY) {
            return new BigDecimal("49.99");
        } else {
            return new BigDecimal("499.99");
        }
    }

    public List<Subscription> getAllSubscriptionsReadyForRenewal() {

        return subscriptionRepository.findAllByStatusAndCompletedOnLessThanEqual(SubscriptionStatus.ACTIVE, LocalDateTime.now());
    }

    public void markSubscriptionAsTerminated(Subscription subscription) {

        subscription.setStatus(SubscriptionStatus.TERMINATED);
        subscription.setCompletedOn(LocalDateTime.now());

        subscriptionRepository.save(subscription);
    }

    public void markSubscriptionAsCompleted(Subscription subscription) {

        User user = subscription.getOwner();

        subscription.setStatus(SubscriptionStatus.COMPLETED);
        subscription.setCompletedOn(LocalDateTime.now());

        user.setTracks(getDefaultTracks(user));
        user.setModules(getDefaultModules(user));

        subscriptionRepository.save(subscription);
    }
}
