package aleksandarskachkov.simracingacademy;


import aleksandarskachkov.simracingacademy.exception.DomainException;
import aleksandarskachkov.simracingacademy.subscription.model.Subscription;
import aleksandarskachkov.simracingacademy.subscription.model.SubscriptionPeriod;
import aleksandarskachkov.simracingacademy.subscription.model.SubscriptionStatus;
import aleksandarskachkov.simracingacademy.subscription.model.SubscriptionType;
import aleksandarskachkov.simracingacademy.subscription.repository.SubscriptionRepository;
import aleksandarskachkov.simracingacademy.subscription.service.SubscriptionService;
import aleksandarskachkov.simracingacademy.track.model.Track;
import aleksandarskachkov.simracingacademy.transaction.model.Transaction;
import aleksandarskachkov.simracingacademy.transaction.model.TransactionStatus;
import aleksandarskachkov.simracingacademy.user.model.Country;
import aleksandarskachkov.simracingacademy.user.model.User;
import aleksandarskachkov.simracingacademy.user.service.UserService;
import aleksandarskachkov.simracingacademy.wallet.model.Wallet;
import aleksandarskachkov.simracingacademy.wallet.repository.WalletRepository;
import aleksandarskachkov.simracingacademy.web.dto.RegisterRequest;
import aleksandarskachkov.simracingacademy.web.dto.UpgradeRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.comparesEqualTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest // Integration Test (Load the complete Spring Application Context - all beans)
public class SubscribeITest {

    @Autowired
    private UserService userService;

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Test
    void subscribeToPlan_thenReturnsFailedTransaction() {

        // Given
        RegisterRequest registerRequest = RegisterRequest.builder()
                .username("Yomov123")
                .password("Test123#")
                .country(Country.BULGARIA)
                .build();
        User registeredUser = userService.register(registerRequest);
        UpgradeRequest upgradeRequest = UpgradeRequest.builder()
                .subscriptionPeriod(SubscriptionPeriod.MONTHLY)
                .walletId(registeredUser.getWallet().getId())
                .build();

        // When
        Transaction transaction = subscriptionService.upgrade(registeredUser, SubscriptionType.PREMIUM, upgradeRequest);

        // Then
        // 1. SUCCEEDED transaction status
        assertThat(transaction.getStatus(), is(TransactionStatus.FAILED));
        assertEquals(TransactionStatus.FAILED, transaction.getStatus());
        // 2. User has ONE ACTIVE subscription - DEFAULT
        Optional<Subscription> defaultSubscription = subscriptionRepository.findByStatusAndOwnerId(SubscriptionStatus.ACTIVE, registeredUser.getId());
        assertTrue(defaultSubscription.isPresent());
        assertEquals(SubscriptionType.DEFAULT, defaultSubscription.get().getType());

        // 3. User's wallet has same money
        Optional<Wallet> userWallet = walletRepository.findByIdAndOwnerId(registeredUser.getWallet().getId(), registeredUser.getId());
        assertTrue(userWallet.isPresent());
        // 0.00 EUR
        assertThat(userWallet.get().getBalance(), comparesEqualTo(new BigDecimal("0.00")));
    }

    @Test
    void subscribeToPremium_happyPath() {

        // Given
        RegisterRequest registerRequest = RegisterRequest.builder()
                .username("Alex123")
                .password("Test123#")
                .country(Country.BULGARIA)
                .build();
        // Wallet has 20 EUR
        User registeredUser = userService.register(registerRequest);
        Wallet wallet = registeredUser.getWallet();
        wallet.setBalance(new BigDecimal("20.00"));
        walletRepository.save(wallet);

        UpgradeRequest upgradeRequest = UpgradeRequest.builder()
                .subscriptionPeriod(SubscriptionPeriod.MONTHLY)
                .walletId(registeredUser.getWallet().getId())
                .build();

        // When
        Transaction transaction = subscriptionService.upgrade(registeredUser, SubscriptionType.PREMIUM, upgradeRequest);

        // Then
        // 1. SUCCEEDED transaction status
        assertThat(transaction.getStatus(), is(TransactionStatus.SUCCEEDED));
        assertEquals(TransactionStatus.SUCCEEDED, transaction.getStatus());
        // 2. User has ONE ACTIVE subscription - PREMIUM
        Optional<Subscription> premiumSubscription = subscriptionRepository.findByStatusAndOwnerId(SubscriptionStatus.ACTIVE, registeredUser.getId());
        assertTrue(premiumSubscription.isPresent());
        assertEquals(SubscriptionType.PREMIUM, premiumSubscription.get().getType());

        // 3. User's wallet has same money
        Optional<Wallet> userWallet = walletRepository.findByIdAndOwnerId(registeredUser.getWallet().getId(), registeredUser.getId());
        assertTrue(userWallet.isPresent());
        // 0.00 EUR
        assertThat(userWallet.get().getBalance(), comparesEqualTo(new BigDecimal("0.01")));
    }

    @Test
    void subscribeToUltimate_happyPath() {

        // Given
        RegisterRequest registerRequest = RegisterRequest.builder()
                .username("Alex123")
                .password("Test123#")
                .country(Country.BULGARIA)
                .build();
        // Wallet has 20 EUR
        User registeredUser = userService.register(registerRequest);
        Wallet wallet = registeredUser.getWallet();
        wallet.setBalance(new BigDecimal("50.00"));
        walletRepository.save(wallet);

        UpgradeRequest upgradeRequest = UpgradeRequest.builder()
                .subscriptionPeriod(SubscriptionPeriod.MONTHLY)
                .walletId(registeredUser.getWallet().getId())
                .build();

        // When
        Transaction transaction = subscriptionService.upgrade(registeredUser, SubscriptionType.ULTIMATE, upgradeRequest);

        // Then
        // 1. SUCCEEDED transaction status
        assertThat(transaction.getStatus(), is(TransactionStatus.SUCCEEDED));
        assertEquals(TransactionStatus.SUCCEEDED, transaction.getStatus());
        // 2. User has ONE ACTIVE subscription - ULTIMATE
        Optional<Subscription> ultimateSubscripton = subscriptionRepository.findByStatusAndOwnerId(SubscriptionStatus.ACTIVE, registeredUser.getId());
        assertTrue(ultimateSubscripton.isPresent());
        assertEquals(SubscriptionType.ULTIMATE, ultimateSubscripton.get().getType());

        // 3. User's wallet has same money
        Optional<Wallet> userWallet = walletRepository.findByIdAndOwnerId(registeredUser.getWallet().getId(), registeredUser.getId());
        assertTrue(userWallet.isPresent());
        // 0.00 EUR
        assertThat(userWallet.get().getBalance(), comparesEqualTo(new BigDecimal("0.01")));
    }



}
