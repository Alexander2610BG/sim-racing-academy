
//package aleksandarskachkov.simracingacademy.scheduler;
//
//import aleksandarskachkov.simracingacademy.subscription.model.Subscription;
//import aleksandarskachkov.simracingacademy.subscription.model.SubscriptionType;
//import aleksandarskachkov.simracingacademy.subscription.service.SubscriptionService;
//import aleksandarskachkov.simracingacademy.transaction.model.Transaction;
//import aleksandarskachkov.simracingacademy.transaction.model.TransactionStatus;
//import aleksandarskachkov.simracingacademy.user.model.User;
//import aleksandarskachkov.simracingacademy.wallet.model.Wallet;
//import aleksandarskachkov.simracingacademy.web.dto.UpgradeRequest;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.Collections;
//import java.util.List;
//import java.util.UUID;
//
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//public class SubscriptionRenewalSchedulerUTest {
//
//    @Mock
//    private SubscriptionService subscriptionService;
//    @Mock
//    private Subscription subscription;
//    @Mock
//    private User subscriptionOwner;
//    @Mock
//    private UpgradeRequest upgradeRequest;
//    @Mock
//    private Transaction transaction;
//    @Mock
//    private Wallet wallet;
//
//    @BeforeEach
//    void setUp() {
//        when(subscription.getOwner()).thenReturn(subscriptionOwner);
//        when(subscriptionOwner.getWallet()).thenReturn(wallet);
//        when(wallet.getId()).thenReturn(UUID.randomUUID());
//    }
//
//    @InjectMocks
//    private SubscriptionRenewalScheduler subscriptionRenewalScheduler;
//
//    @Test
//    void givenNoSubscription_whenRenewSubscriptions_thenNoSubscriptionsFound() {
//
//        // Given
//        when(subscriptionService.getAllSubscriptionsReadyForRenewal()).thenReturn(Collections.emptyList());
//
//        // When
//        subscriptionRenewalScheduler.renewSubscriptions();
//
//        // Then
//        verify(subscriptionService).getAllSubscriptionsReadyForRenewal();
//    }
//
//    @Test
//    void givenRenewalAllowed_whenRenewSubscriptions_thenUpgradeSubscription() {
//
//        // Given
//        when(subscription.isRenewalAllowed()).thenReturn(true);
//        when(subscriptionService.upgrade(eq(subscriptionOwner), eq(SubscriptionType.PREMIUM), any(UpgradeRequest.class)))
//                .thenReturn(transaction);
//        when(transaction.getStatus()).thenReturn(TransactionStatus.SUCCEEDED);
//        when(subscriptionService.getAllSubscriptionsReadyForRenewal()).thenReturn(List.of(subscription));
//
//        // When
//        subscriptionRenewalScheduler.renewSubscriptions();
//
//        // Then
//        verify(subscriptionService).upgrade(eq(subscriptionOwner), eq(SubscriptionType.PREMIUM), any(UpgradeRequest.class));
//        verify(subscriptionService).markSubscriptionAsCompleted(subscription);
//    }
//
//    @Test
//    void givenFailedTransaction_whenRenewSubscriptions_thenTerminateAndCreateDefaultSubscription() {
//        // Given
//        when(subscription.isRenewalAllowed()).thenReturn(true);
//        when(subscriptionService.upgrade(eq(subscriptionOwner), eq(SubscriptionType.PREMIUM), any(UpgradeRequest.class)))
//                .thenReturn(transaction);
//        when(transaction.getStatus()).thenReturn(TransactionStatus.FAILED);
//        when(subscriptionService.getAllSubscriptionsReadyForRenewal()).thenReturn(List.of(subscription));
//
//        // When
//        subscriptionRenewalScheduler.renewSubscriptions();
//
//        // Then
//        verify(subscriptionService).markSubscriptionAsTerminated(subscription);
//        verify(subscriptionService).createDefaultSubscription(subscriptionOwner);
//    }
//
//}
