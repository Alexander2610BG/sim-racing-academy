package aleksandarskachkov.simracingacademy.wallet.service;

import aleksandarskachkov.simracingacademy.exception.DomainException;
import aleksandarskachkov.simracingacademy.security.AuthenticationMetadata;
import aleksandarskachkov.simracingacademy.transaction.model.Transaction;
import aleksandarskachkov.simracingacademy.transaction.model.TransactionStatus;
import aleksandarskachkov.simracingacademy.transaction.model.TransactionType;
import aleksandarskachkov.simracingacademy.transaction.service.TransactionService;
import aleksandarskachkov.simracingacademy.user.model.User;
import aleksandarskachkov.simracingacademy.wallet.model.Wallet;
import aleksandarskachkov.simracingacademy.wallet.model.WalletStatus;
import aleksandarskachkov.simracingacademy.wallet.repository.WalletRepository;
import aleksandarskachkov.simracingacademy.web.dto.PaymentNotificationEvent;
import aleksandarskachkov.simracingacademy.web.dto.TransferRequest;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class WalletService {

    private static final String SIM_RACING_ACADEMY = "Sim Racing Academy Ltd";


    private final WalletRepository walletRepository;
    private final TransactionService transactionService;
    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    public WalletService(WalletRepository walletRepository, TransactionService transactionService, ApplicationEventPublisher eventPublisher) {
        this.walletRepository = walletRepository;
        this.transactionService = transactionService;
        this.eventPublisher = eventPublisher;
    }

    public Wallet createNewWallet(User user) {

        Wallet wallet = walletRepository.save(intializeWallet(user));
        log.info("Successfully create new wallet with id [%s] and balance [%.2f]".formatted(wallet.getId(), wallet.getBalance()));

        return wallet;
    }

    public Transaction transferFunds(User sender, TransferRequest transferRequest) {

        Wallet senderWallet = getWalletById(transferRequest.getFromWalletId());
        Optional<Wallet> receiverWalletOptional = walletRepository.findAllByOwnerUsername(transferRequest.getToUsername())
                .stream()
                .filter(w -> w.getStatus() == WalletStatus.ACTIVE)
                .findFirst();

        String transferDescription = "Transfer from %s to %s, for %.2f".formatted(sender.getUsername(), transferRequest.getToUsername(), transferRequest.getAmount());


        // failed transaction
        if (receiverWalletOptional.isEmpty()) {

            return transactionService.createNewTransaction(sender,
                    senderWallet.getId().toString(),
                    transferRequest.getToUsername(),
                    transferRequest.getAmount(),
                    senderWallet.getBalance(),
                    senderWallet.getCurrency(),
                    TransactionType.WITHDRAWAL,
                    TransactionStatus.FAILED,
                    transferDescription,
                    "Invalid criteria for transfer"
                    );
        }

        Transaction withdrawal = charge(sender, senderWallet.getId(), transferRequest.getAmount(), transferDescription);
        if (withdrawal.getStatus() == TransactionStatus.FAILED) {
            return withdrawal;
        }

        Wallet receiverWallet = receiverWalletOptional.get();
        receiverWallet.setBalance(receiverWallet.getBalance().add(transferRequest.getAmount()));
        receiverWallet.setUpdatedOn(LocalDateTime.now());

        walletRepository.save(receiverWallet);
        transactionService.createNewTransaction(receiverWallet.getOwner(),
                senderWallet.getId().toString(),
                receiverWallet.getId().toString(),
                transferRequest.getAmount(),
                receiverWallet.getBalance(),
                receiverWallet.getCurrency(),
                TransactionType.DEPOSIT,
                TransactionStatus.SUCCEEDED,
                transferDescription,
                null
                );

        return withdrawal;
    }

    @Transactional
    public Transaction charge(User user, UUID walletId, BigDecimal amount, String description) {

        Wallet wallet = getWalletById(walletId);

        String failureReason = null;
        boolean isFailedTransaction = false;
        if (wallet.getStatus() == WalletStatus.INACTIVE) {
            failureReason = "Inactive wallet status";
            isFailedTransaction = true;
        }
        if (wallet.getBalance().compareTo(amount) < 0) {
            failureReason = "Insufficient balance";
            isFailedTransaction = true;
        }

        if (isFailedTransaction) {
            return transactionService.createNewTransaction(user,
                    wallet.getId().toString(),
                    SIM_RACING_ACADEMY,
                    amount,
                    wallet.getBalance(),
                    wallet.getCurrency(),
                    TransactionType.WITHDRAWAL,
                    TransactionStatus.FAILED,
                    description,
                    failureReason);
        }

            wallet.setBalance(wallet.getBalance().subtract(amount));
            wallet.setUpdatedOn(LocalDateTime.now());

            walletRepository.save(wallet);

            PaymentNotificationEvent event = PaymentNotificationEvent.builder()
                    .userId(user.getId())
                    .paymentTime(LocalDateTime.now())
                    .email(user.getEmail())
                    .amount(amount)
                    .build();
            eventPublisher.publishEvent(event);

            return transactionService.createNewTransaction(user,
                    wallet.getId().toString(),
                    SIM_RACING_ACADEMY,
                    amount,
                    wallet.getBalance(),
                    wallet.getCurrency(),
                    TransactionType.WITHDRAWAL,
                    TransactionStatus.SUCCEEDED,
                    description,
                    null
                    );
    }

    private Wallet getWalletById(UUID walletId) {
        return walletRepository.findById(walletId)
                .orElseThrow(() -> new DomainException("Wallet with id [%s] does not exist.".formatted(walletId)));
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

    public Map<UUID, List<Transaction>> getLastFiveTransactions(Wallet wallet) {

        Map<UUID, List<Transaction>> transactionByWalletId = new LinkedHashMap<>();

        List<Transaction> lastFiveTransactions = transactionService.getLastFiveTransactionsByWalletId(wallet);
        transactionByWalletId.put(wallet.getId(), lastFiveTransactions);

        return transactionByWalletId;
    }

    public Wallet getWalletByUser(UUID id) {
        return walletRepository.findByOwnerId(id);
    }

    public void switchStatus(UUID walletId, UUID ownerId) {

        Optional<Wallet> optionalWallet = walletRepository.findByIdAndOwnerId(walletId, ownerId);

        if (optionalWallet.isEmpty()) {
            throw new DomainException("Wallet with id [%s] does not belong to user with id [%s].".formatted(walletId, ownerId));
        }

        Wallet wallet = optionalWallet.get();
        if (wallet.getStatus() == WalletStatus.ACTIVE) {
            wallet.setStatus(WalletStatus.INACTIVE);
        } else {
            wallet.setStatus(WalletStatus.ACTIVE);
        }

        walletRepository.save(wallet);
    }

    @Transactional
    public Transaction topUp(UUID walletId, BigDecimal amount) {

        Wallet wallet = getWalletById(walletId);
        String transactionDescription = "Top up %.2f".formatted(amount.doubleValue());

        if (wallet.getStatus() == WalletStatus.INACTIVE) {
            return transactionService.createNewTransaction(wallet.getOwner(),
                    SIM_RACING_ACADEMY,
                    walletId.toString(),
                    amount,
                    wallet.getBalance(),
                    wallet.getCurrency(),
                    TransactionType.DEPOSIT,
                    TransactionStatus.FAILED,
                    transactionDescription, "Inactive wallet");
        }

        wallet.setBalance(wallet.getBalance().add(amount));
        wallet.setUpdatedOn(LocalDateTime.now());

        walletRepository.save(wallet);

        Transaction transaction = transactionService.createNewTransaction(wallet.getOwner(),
                SIM_RACING_ACADEMY,
                walletId.toString(),
                amount,
                wallet.getBalance(),
                wallet.getCurrency(),
                TransactionType.DEPOSIT,
                TransactionStatus.SUCCEEDED,
                transactionDescription, null);

        return transaction;
    }
}
