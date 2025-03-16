package aleksandarskachkov.simracingacademy.scheduler;

import aleksandarskachkov.simracingacademy.subscription.model.Subscription;
import aleksandarskachkov.simracingacademy.subscription.model.SubscriptionPeriod;
import aleksandarskachkov.simracingacademy.subscription.model.SubscriptionType;
import aleksandarskachkov.simracingacademy.subscription.service.SubscriptionService;
import aleksandarskachkov.simracingacademy.transaction.model.Transaction;
import aleksandarskachkov.simracingacademy.transaction.model.TransactionStatus;
import aleksandarskachkov.simracingacademy.user.model.User;
import aleksandarskachkov.simracingacademy.web.dto.UpgradeRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Slf4j
@Component
public class SubscriptionRenewalScheduler {

    private final SubscriptionService subscriptionService;

    @Autowired
    public SubscriptionRenewalScheduler(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    // checks in every 20 seconds for renew
    @Scheduled(fixedRate = 20000)
    public void renewSubscriptions() {

        List<Subscription> subscriptions = subscriptionService.getAllSubscriptionsReadyForRenewal();

        if (subscriptions.isEmpty()) {
            log.info("No subscriptions found for renewal.");
        }

        for (Subscription subscription : subscriptions) {

            if (subscription.isRenewalAllowed()) {

                User subscriptionOwner = subscription.getOwner();
                SubscriptionType type = subscription.getType();
                SubscriptionPeriod period = subscription.getPeriod();
                UUID walletId = subscriptionOwner.getWallet().getId();
                UpgradeRequest upgradeRequest = UpgradeRequest.builder()
                        .subscriptionPeriod(period)
                        .walletId(walletId)
                        .build();

                Transaction transaction = subscriptionService.upgrade(subscriptionOwner, type, upgradeRequest);
                if (transaction.getStatus() == TransactionStatus.FAILED) {
                    subscriptionService.markSubscriptionAsTerminated(subscription);
                    subscriptionService.createDefaultSubscription(subscription.getOwner());
                } else {
                    // kogato svurshi sub-a se suzdava default
                    subscriptionService.markSubscriptionAsCompleted(subscription);
//                    subscriptionService.createDefaultSubscription(subscription.getOwner());
                }
            }
        }
    }
}
