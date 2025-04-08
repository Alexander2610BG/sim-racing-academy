
package aleksandarskachkov.simracingacademy.scheduler;

import aleksandarskachkov.simracingacademy.subscription.model.Subscription;
import aleksandarskachkov.simracingacademy.subscription.model.SubscriptionPeriod;
import aleksandarskachkov.simracingacademy.subscription.model.SubscriptionStatus;
import aleksandarskachkov.simracingacademy.subscription.model.SubscriptionType;
import aleksandarskachkov.simracingacademy.subscription.service.SubscriptionService;
import aleksandarskachkov.simracingacademy.transaction.model.Transaction;
import aleksandarskachkov.simracingacademy.transaction.model.TransactionStatus;
import aleksandarskachkov.simracingacademy.user.model.User;
import aleksandarskachkov.simracingacademy.wallet.model.Wallet;
import aleksandarskachkov.simracingacademy.web.dto.UpgradeRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SubscriptionRenewalSchedulerUTest {

    @Mock
    private SubscriptionService subscriptionService;
    @Mock
    private Subscription subscription;
    @Mock
    private User subscriptionOwner;
    @Mock
    private UpgradeRequest upgradeRequest;
    @Mock
    private Transaction transaction;
    @Mock
    private Wallet wallet;

//    @BeforeEach
//    void setUp() {
//        User user = new User();
//        user.setId(UUID.randomUUID());
//
//        Subscription activeSubscription = new Subscription();
//        activeSubscription.setOwner(user);
//        activeSubscription.setRenewalAllowed(true);
//        activeSubscription.setType(SubscriptionType.PREMIUM);
//        activeSubscription.setPeriod(SubscriptionPeriod.MONTHLY);
//
//        Subscription nonRenewableSubscription = new Subscription();
//        nonRenewableSubscription.setOwner(user);
//        nonRenewableSubscription.setRenewalAllowed(false);
//
//        Transaction  successfulTransaction = new Transaction();
//        successfulTransaction.setStatus(TransactionStatus.SUCCEEDED);
//
//        Transaction failedTransaction = new Transaction();
//        failedTransaction.setStatus(TransactionStatus.FAILED);
//    }

//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//
//        // Initialize a mock user
//        User user = mock(User.class);
//        when(user.getWallet()).thenReturn(mock(User.Wallet.class));
//        when(user.getWallet().getId()).thenReturn(UUID.randomUUID());
//
//        // Initialize a mock subscription
//        subscription = mock(Subscription.class);
//        when(subscription.getOwner()).thenReturn(user);
//        when(subscription.isRenewalAllowed()).thenReturn(true);
//    }

//    @BeforeEach
//    void setUp() {
//        when(subscription.getOwner()).thenReturn(subscriptionOwner);
//        when(subscriptionOwner.getWallet()).thenReturn(wallet);
//        when(wallet.getId()).thenReturn(UUID.randomUUID());
//    }

    @InjectMocks
    private SubscriptionRenewalScheduler subscriptionRenewalScheduler;

    @Test
    void givenNoSubscription_whenRenewSubscriptions_thenNoSubscriptionsFound() {

        // Given
        when(subscriptionService.getAllSubscriptionsReadyForRenewal()).thenReturn(Collections.emptyList());

        // When
        subscriptionRenewalScheduler.renewSubscriptions();

        // Then
        verify(subscriptionService).getAllSubscriptionsReadyForRenewal();
    }



    @Test
    void renewSubscriptions_NoSubscriptions_ShouldDoNothing() {
        when(subscriptionService.getAllSubscriptionsReadyForRenewal()).thenReturn(List.of());

        subscriptionRenewalScheduler.renewSubscriptions();

        verify(subscriptionService, times(1)).getAllSubscriptionsReadyForRenewal();
        verifyNoMoreInteractions(subscriptionService);
    }

    @Test
    void processSubscriptions_shouldUpgradeSuccessfulTransaction() {
        // Given
        Subscription subscription = mock(Subscription.class);
        User owner = mock(User.class);
        Wallet wallet = mock(Wallet.class);
        UUID walletId = UUID.randomUUID();
        SubscriptionType type = SubscriptionType.PREMIUM;
        SubscriptionPeriod period = SubscriptionPeriod.MONTHLY;
        Transaction successfulTransaction = Transaction.builder().status(TransactionStatus.SUCCEEDED).build();

        List<Subscription> subscriptions = List.of(subscription);

        when(subscription.isRenewalAllowed()).thenReturn(true);
        when(subscription.getOwner()).thenReturn(owner);
        when(subscription.getType()).thenReturn(type);
        when(subscription.getPeriod()).thenReturn(period);
        when(owner.getWallet()).thenReturn(wallet);
        when(wallet.getId()).thenReturn(walletId);
        when(subscriptionService.upgrade(eq(owner), eq(type), any(UpgradeRequest.class)))
                .thenReturn(successfulTransaction);

        // When
        subscriptionRenewalScheduler.renewSubscriptions(); // Replace with your actual method name

        // Then
        verify(subscriptionService).upgrade(eq(owner), eq(type), any(UpgradeRequest.class));
        verify(subscriptionService).markSubscriptionAsCompleted(subscription);
        verify(subscriptionService, never()).markSubscriptionAsTerminated(subscription);
        verify(subscriptionService, never()).createDefaultSubscription(any());
    }

//    @Test
//    void renewSubscriptions_subscriptionSuccessfulRenewal() {
//        // Given: One subscription is available for renewal and renewal is allowed
//        when(subscriptionService.getAllSubscriptionsReadyForRenewal()).thenReturn(List.of(subscription));
//
//        // Given: Successful renewal transaction
//        Transaction successfulTransaction = mock(Transaction.class);
//        when(successfulTransaction.getStatus()).thenReturn(TransactionStatus.SUCCEEDED);
//        when(subscriptionService.upgrade(any(User.class), any(SubscriptionType.class), any(UpgradeRequest.class)))
//                .thenReturn(successfulTransaction);
//
//        // When: The scheduler runs to renew subscriptions
//        subscriptionRenewalScheduler.renewSubscriptions();
//
//        // Then: Verify that subscription upgrade was attempted
//        verify(subscriptionService, times(1)).getAllSubscriptionsReadyForRenewal();
//        verify(subscriptionService, times(1)).upgrade(any(User.class), any(SubscriptionType.class), any(UpgradeRequest.class));
//        verify(subscriptionService, times(1)).markSubscriptionAsCompleted(subscription);
//        verifyNoMoreInteractions(subscriptionService);
//    }
//
//
//    @Test
//    void renewSubscriptions_WithFailedTransaction_ShouldTerminateAndCreateDefault() {
//
//        User user = new User();
//        user.setId(UUID.randomUUID());
//
//        when(subscriptionService.getAllSubscriptionsReadyForRenewal()).thenReturn(List.of(Subscription.builder().status(SubscriptionStatus.ACTIVE).build()));
//        when(subscriptionService.upgrade(any(), any(), any())).thenReturn(Transaction.builder().status(TransactionStatus.FAILED).build());
//
//        subscriptionRenewalScheduler.renewSubscriptions();
//
//        verify(subscriptionService).getAllSubscriptionsReadyForRenewal();
//        verify(subscriptionService).upgrade(eq(user), eq(SubscriptionType.PREMIUM), any(UpgradeRequest.class));
//        verify(subscriptionService).markSubscriptionAsTerminated(Subscription.builder().status(SubscriptionStatus.ACTIVE).build());
//        verify(subscriptionService).createDefaultSubscription(user);
//        verify(subscriptionService, never()).markSubscriptionAsCompleted(any());
//    }

/*    @Test
    void givenRenewalAllowed_whenRenewSubscriptions_thenUpgradeSubscription() {

        // Given
        when(subscription.isRenewalAllowed()).thenReturn(true);
        when(subscriptionService.upgrade(eq(subscriptionOwner), eq(SubscriptionType.PREMIUM), any(UpgradeRequest.class)))
                .thenReturn(transaction);
        when(transaction.getStatus()).thenReturn(TransactionStatus.SUCCEEDED);
        when(subscriptionService.getAllSubscriptionsReadyForRenewal()).thenReturn(List.of(subscription));

        // When
        subscriptionRenewalScheduler.renewSubscriptions();

        // Then
        verify(subscriptionService).upgrade(eq(subscriptionOwner), eq(SubscriptionType.PREMIUM), any(UpgradeRequest.class));
        verify(subscriptionService).markSubscriptionAsCompleted(subscription);
    }

    @Test
    void givenFailedTransaction_whenRenewSubscriptions_thenTerminateAndCreateDefaultSubscription() {
        // Given
        when(subscription.isRenewalAllowed()).thenReturn(true);
        when(subscriptionService.upgrade(eq(subscriptionOwner), eq(SubscriptionType.PREMIUM), any(UpgradeRequest.class)))
                .thenReturn(transaction);
        when(transaction.getStatus()).thenReturn(TransactionStatus.FAILED);
        when(subscriptionService.getAllSubscriptionsReadyForRenewal()).thenReturn(List.of(subscription));

        // When
        subscriptionRenewalScheduler.renewSubscriptions();

        // Then
        verify(subscriptionService).markSubscriptionAsTerminated(subscription);
        verify(subscriptionService).createDefaultSubscription(subscriptionOwner);
    }*/

}
