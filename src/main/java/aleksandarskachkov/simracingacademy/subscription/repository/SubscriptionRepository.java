package aleksandarskachkov.simracingacademy.subscription.repository;

import aleksandarskachkov.simracingacademy.subscription.model.Subscription;
import aleksandarskachkov.simracingacademy.subscription.model.SubscriptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, UUID> {
    Optional<Subscription> findByStatusAndOwnerId(SubscriptionStatus subscriptionStatus, UUID id);

    List<Subscription> findAllByStatusAndCompletedOnLessThanEqual(SubscriptionStatus subscriptionStatus, LocalDateTime now);
}
