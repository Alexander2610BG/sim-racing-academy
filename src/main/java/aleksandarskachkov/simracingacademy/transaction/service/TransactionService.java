package aleksandarskachkov.simracingacademy.transaction.service;

import aleksandarskachkov.simracingacademy.exception.DomainException;
import aleksandarskachkov.simracingacademy.exception.TransactionDoesntExist;
import aleksandarskachkov.simracingacademy.notification.service.NotificationService;
import aleksandarskachkov.simracingacademy.transaction.model.Transaction;
import aleksandarskachkov.simracingacademy.transaction.model.TransactionStatus;
import aleksandarskachkov.simracingacademy.transaction.model.TransactionType;
import aleksandarskachkov.simracingacademy.transaction.repository.TransactionRepository;
import aleksandarskachkov.simracingacademy.user.model.User;
import aleksandarskachkov.simracingacademy.wallet.model.Wallet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final NotificationService notificationService;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository, NotificationService notificationService) {
        this.transactionRepository = transactionRepository;
        this.notificationService = notificationService;
    }

    public Transaction createNewTransaction(User owner, String sender, String receiver, BigDecimal transactionAmount, BigDecimal balanceLeft, Currency currency, TransactionType type, TransactionStatus transactionStatus, String transactionDescription, String failureReason) {

        Transaction transaction = Transaction.builder()
                .owner(owner)
                .sender(sender)
                .receiver(receiver)
                .amount(transactionAmount)
                .balanceLeft(balanceLeft)
                .currency(currency)
                .type(type)
                .status(transactionStatus)
                .description(transactionDescription)
                .failureReason(failureReason)
                .createdOn(LocalDateTime.now())
                .build();

        String emailBody = "%s transaction was successful processed for you with amount %.2f EUR!".formatted(transaction.getType(), transaction.getAmount());
        notificationService.sendNotification(transaction.getOwner().getId(), "New Sim Racing Academy Transaction", emailBody);

       return transactionRepository.save(transaction);
    }

    public List<Transaction> getAllByOwnerId(UUID userId) {

      return transactionRepository.findAllByOwnerIdOrderByCreatedOnDesc(userId);
    }

    public List<Transaction> getLastFiveTransactionsByWalletId(Wallet wallet) {

        List<Transaction> lastFiveTransactions = transactionRepository.findAllBySenderOrReceiverOrderByCreatedOnDesc(wallet.getId().toString(), wallet.getId().toString())
                .stream()
                .filter(t -> t.getOwner().getId() == wallet.getOwner().getId())
                .filter(t -> t.getStatus() == TransactionStatus.SUCCEEDED)
                .limit(5)
                .collect(Collectors.toList());

        return lastFiveTransactions;
    }

    public Transaction getById(UUID id) {

        return transactionRepository.findById(id).orElseThrow(() -> new TransactionDoesntExist("Transaction with id [%s] does not exits.".formatted(id)));
    }
}
