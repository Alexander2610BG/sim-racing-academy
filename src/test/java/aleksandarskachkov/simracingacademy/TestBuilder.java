package aleksandarskachkov.simracingacademy;


import aleksandarskachkov.simracingacademy.module.model.Module;
import aleksandarskachkov.simracingacademy.subscription.model.Subscription;
import aleksandarskachkov.simracingacademy.subscription.model.SubscriptionPeriod;
import aleksandarskachkov.simracingacademy.subscription.model.SubscriptionStatus;
import aleksandarskachkov.simracingacademy.subscription.model.SubscriptionType;
import aleksandarskachkov.simracingacademy.track.model.Track;
import aleksandarskachkov.simracingacademy.transaction.model.Transaction;
import aleksandarskachkov.simracingacademy.transaction.model.TransactionStatus;
import aleksandarskachkov.simracingacademy.transaction.model.TransactionType;
import aleksandarskachkov.simracingacademy.user.model.Country;
import aleksandarskachkov.simracingacademy.user.model.User;
import aleksandarskachkov.simracingacademy.user.model.UserRole;
import aleksandarskachkov.simracingacademy.wallet.model.Wallet;
import aleksandarskachkov.simracingacademy.wallet.model.WalletStatus;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.List;
import java.util.UUID;

@UtilityClass
public class TestBuilder {

    public static User aRandomUser() {

        User user = User.builder()
                .id(UUID.randomUUID())
                .username("Alex123")
                .password("123123")
                .role(UserRole.USER)
                .country(Country.BULGARIA)
                .isActive(true)
                .createdOn(LocalDateTime.now())
                .updatedOn(LocalDateTime.now())
                .build();

        Wallet wallet = Wallet.builder()
                .owner(user)
                .balance(BigDecimal.ZERO)
                .status(WalletStatus.ACTIVE)
                .currency(Currency.getInstance("EUR"))
                .updatedOn(LocalDateTime.now())
                .createdOn(LocalDateTime.now())
                .build();

        Subscription subscription = Subscription.builder()
                .id(UUID.randomUUID())
                .type(SubscriptionType.PREMIUM)
                .tracks(List.of(Track.builder().build()))
                .modules(List.of(Module.builder().build()))
                .price(BigDecimal.ZERO)
                .status(SubscriptionStatus.ACTIVE)
                .period(SubscriptionPeriod.MONTHLY)
                .createdOn(LocalDateTime.now())
                .completedOn(LocalDateTime.now().plusMonths(1))
                .owner(user)
                .renewalAllowed(true)
                .build();

        user.setSubscriptions(List.of(subscription));
        user.setWallet(wallet);

        return user;
    }

    public static Transaction aRandomTransaction() {
       return Transaction.builder()
                .id(UUID.randomUUID())
                .owner(aRandomUser())
                .sender("asdf")
                .receiver("asdf")
                .amount(BigDecimal.valueOf(15))
                .balanceLeft(BigDecimal.ZERO)
                .createdOn(LocalDateTime.now())
                .currency(Currency.getInstance("EUR"))
                .type(TransactionType.WITHDRAWAL)
                .status(TransactionStatus.SUCCEEDED)
                .description("asdf")
                .build();
    }
}