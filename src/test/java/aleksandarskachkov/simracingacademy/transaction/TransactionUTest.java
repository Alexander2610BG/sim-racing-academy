package aleksandarskachkov.simracingacademy.transaction;

import aleksandarskachkov.simracingacademy.notification.service.NotificationService;
import aleksandarskachkov.simracingacademy.transaction.model.Transaction;
import aleksandarskachkov.simracingacademy.transaction.model.TransactionStatus;
import aleksandarskachkov.simracingacademy.transaction.repository.TransactionRepository;
import aleksandarskachkov.simracingacademy.transaction.service.TransactionService;
import aleksandarskachkov.simracingacademy.user.model.User;
import aleksandarskachkov.simracingacademy.wallet.model.Wallet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransactionUTest {

    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private TransactionService transactionService;


//    @Test
//    void givenWallet_whenGetLastFiveTransactions_thenReturnsLastFiveTransactions() {
//
//        // Given
//        Wallet wallet = Wallet.builder()
//                .id(UUID.randomUUID())
//                .owner(User.builder().id(UUID.randomUUID()).build())
//                .build();
//        when(transactionRepository.findAllBySenderOrReceiverOrderByCreatedOnDesc(wallet.getId().toString(), wallet.getId().toString()));
//
//        // When
//        transactionService.getLastFiveTransactionsByWalletId(wallet);
//
//        // Then
//        assertEquals
//    }
//
//    @Test
//    void givenWallet_whenGetLastFiveTransactions_thenReturnsLastFiveTransactions() {
//        // Given
//        UUID walletId = UUID.randomUUID();
//        Wallet wallet = Wallet.builder()
//                .id(walletId)
////                .owner(User.builder().id(UUID.randomUUID()).build())
//                .build();
//
//        // Create test transactions
//        Transaction t1 = Transaction.builder()
//                .id(UUID.randomUUID())
//                .status(TransactionStatus.SUCCEEDED)
//                .owner(wallet.getOwner())
//                .build();
//        Transaction t2 = Transaction.builder()
//                .id(UUID.randomUUID())
//                .status(TransactionStatus.SUCCEEDED)
//                .owner(wallet.getOwner())
//                .build();
//        Transaction t3 = Transaction.builder()
//                .id(UUID.randomUUID())
//                .status(TransactionStatus.FAILED) // Should be filtered out
//                .owner(wallet.getOwner())
//                .build();
//        Transaction t4 = Transaction.builder()
//                .id(UUID.randomUUID())
//                .status(TransactionStatus.SUCCEEDED)
//                .owner(wallet.getOwner())
//                .build();
//        Transaction t5 = Transaction.builder()
//                .id(UUID.randomUUID())
//                .status(TransactionStatus.SUCCEEDED)
//                .owner(User.builder().id(UUID.randomUUID()).build())
//                .build();
////        Transaction t6 = Transaction.builder()
////                .id(UUID.randomUUID())
////                .status(TransactionStatus.SUCCEEDED)
////                .owner(wallet.getOwner())
////                .build();
//
//        List<Transaction> mockTransactions = Arrays.asList(t1, t2, t3, t4, t5);
//
//        when(transactionRepository.findAllBySenderOrReceiverOrderByCreatedOnDesc(
//                walletId.toString(),
//                walletId.toString()))
//                .thenReturn(mockTransactions);
//
//        // When
//        List<Transaction> result = transactionService.getLastFiveTransactionsByWalletId(wallet);
//
//        // Then
//        assertEquals(4, result.size()); // Only 4 transactions meet the criteria
//        assertEquals(6L, result.get(0).getId()); // Verify order (newest first)
//        assertEquals(4L, result.get(1).getId());
//        assertEquals(2L, result.get(2).getId());
//        assertEquals(1L, result.get(3).getId());
//
//        // Verify all returned transactions have correct status and owner
//        result.forEach(transaction -> {
//            assertEquals(TransactionStatus.SUCCEEDED, transaction.getStatus());
//            assertEquals(wallet.getOwner().getId(), transaction.getOwner().getId());
//        });
//    }
//
//    @Test
//    void givenWalletWithNoTransactions_whenGetLastFiveTransactions_thenReturnsEmptyList() {
//        // Given
//        Wallet wallet = Wallet.builder()
//                .id(UUID.randomUUID())
//                .build();
//
//        when(transactionRepository.findAllBySenderOrReceiverOrderByCreatedOnDesc(
//                wallet.getId().toString(),
//                wallet.getId().toString()))
//                .thenReturn(List.of());
//
//        // When
//        List<Transaction> result = transactionService.getLastFiveTransactionsByWalletId(wallet);
//
//        // Then
//        assertEquals(0, result.size());
//    }
}
