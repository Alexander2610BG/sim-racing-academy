package aleksandarskachkov.simracingacademy.wallet.service;

import aleksandarskachkov.simracingacademy.user.model.User;
import aleksandarskachkov.simracingacademy.wallet.model.Wallet;
import aleksandarskachkov.simracingacademy.wallet.model.WalletStatus;
import aleksandarskachkov.simracingacademy.wallet.repository.WalletRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;

@Slf4j
@Service
public class WalletService {

    private final WalletRepository walletRepository;

    @Autowired
    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    public Wallet createNewWallet(User user) {

        Wallet wallet = walletRepository.save(intializeWallet(user));
        log.info("Successfully create new wallet with id [%s] and balance [%.2f]".formatted(wallet.getId(), wallet.getBalance()));

        return wallet;
    }

    private Wallet intializeWallet(User user) {

        return Wallet.builder()
                .owner(user)
                .status(WalletStatus.ACTIVE)
                .balance(new BigDecimal("0.00"))
                .currency(Currency.getInstance("EUR"))
                .createdOn(LocalDateTime.now())
                .updatedOn(LocalDateTime.now())
                .build();
    }
}
