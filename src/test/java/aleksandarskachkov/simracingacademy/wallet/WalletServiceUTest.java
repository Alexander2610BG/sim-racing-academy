package aleksandarskachkov.simracingacademy.wallet;

import aleksandarskachkov.simracingacademy.transaction.model.Transaction;
import aleksandarskachkov.simracingacademy.transaction.model.TransactionStatus;
import aleksandarskachkov.simracingacademy.transaction.model.TransactionType;
import aleksandarskachkov.simracingacademy.transaction.service.TransactionService;
import aleksandarskachkov.simracingacademy.user.model.User;
import aleksandarskachkov.simracingacademy.wallet.model.Wallet;
import aleksandarskachkov.simracingacademy.wallet.model.WalletStatus;
import aleksandarskachkov.simracingacademy.wallet.repository.WalletRepository;
import aleksandarskachkov.simracingacademy.wallet.service.WalletService;
import aleksandarskachkov.simracingacademy.web.dto.PaymentNotificationEvent;
import aleksandarskachkov.simracingacademy.web.dto.TransferRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WalletServiceUTest {


    @Mock
    private WalletRepository walletRepository;
    @Mock
    private TransactionService transactionService;
    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private WalletService walletService;

    private static final String SIM_RACING_ACADEMY = "SIM_RACING_ACADEMY";

    @Test
    void givenNotExistingSenderAndTransferRequest_whenTransferFunds_thenReturnFailedTransaction() {

        // Given

        User sender = User.builder()
                .id(UUID.randomUUID())
                .build();

        TransferRequest transferRequest = TransferRequest.builder()
                .fromWalletId(UUID.randomUUID())
                .toUsername("receiver")
                .amount(BigDecimal.valueOf(100))
                .build();

        Wallet senderWallet = Wallet.builder()
                .id(transferRequest.getFromWalletId())
                .balance(BigDecimal.valueOf(1000))
                .status(WalletStatus.ACTIVE)
                .build();

        when(walletRepository.findAllByOwnerUsername(transferRequest.getToUsername())).thenReturn(Optional.empty());

        // When
        Transaction result = walletService.transferFunds(sender, transferRequest);

        // Then
        assertEquals(TransactionStatus.FAILED, result.getStatus());
//        verify(transactionService).createNewTransaction(
//                eq(sender),
//                eq(senderWallet.getId().toString()),
//                eq(transferRequest.getToUsername()),
//                eq(transferRequest.getAmount()),
//                eq(senderWallet.getBalance()),
//                eq(senderWallet.getCurrency()),
//                eq(TransactionType.WITHDRAWAL),
//                eq(TransactionStatus.FAILED),
//                anyString(),
//                eq("Invalid criteria for transfer")
//        );

    }

    @Test
    void givenWalletWithActiveStatusWithSufficientBalance_whenCharge_thenChargeAndNewSuccessTransaction() {

        // Given
        User user = User.builder()
                .id(UUID.randomUUID())
                .build();

        Wallet wallet = Wallet.builder()
                .id(UUID.randomUUID())
                .balance(BigDecimal.valueOf(100))
                .status(WalletStatus.ACTIVE)
                .owner(user)
                .currency(Currency.getInstance("EUR"))
                .build();

        BigDecimal amount = BigDecimal.valueOf(50);

        Transaction expectedTransaction = Transaction.builder()
                .status(TransactionStatus.SUCCEEDED)
                .build();

        when(walletRepository.findById(wallet.getId())).thenReturn(Optional.of(wallet));
        when(walletRepository.save(wallet)).thenReturn(wallet);
        when(transactionService.createNewTransaction(
                any(), anyString(), anyString(), any(), any(), any(), any(), any(), any(), any()
        )).thenReturn(expectedTransaction);

        // When
        Transaction result = walletService.charge(user, wallet.getId(), amount, "Charge Payment");

        // Then
        assertEquals(TransactionStatus.SUCCEEDED, result.getStatus());
        assertEquals(BigDecimal.valueOf(50), wallet.getBalance());
        verify(walletRepository, times(1)).save(wallet);
        verify(eventPublisher, times(1)).publishEvent(any(PaymentNotificationEvent.class));
//        verify(transactionService, times(1)).createNewTransaction(
//                user,
//                wallet.getId().toString(),
//                SIM_RACING_ACADEMY,
//                amount,
//                wallet.getBalance(),
//                wallet.getCurrency(),
//                TransactionType.WITHDRAWAL,
//                TransactionStatus.SUCCEEDED,
//                "Charge Payment",
//                null
//        );

    }

    @Test
    void givenWalletWithActiveStatusWithInsufficientBalance_whenCharge_thenReturnNewFailedTransaction() {

        // Given
        User user = User.builder()
                .id(UUID.randomUUID())
                .build();

        Wallet wallet = Wallet.builder()
                .id(UUID.randomUUID())
                .balance(BigDecimal.valueOf(20))
                .status(WalletStatus.INACTIVE)
                .owner(user)
                .build();

        BigDecimal amount = BigDecimal.valueOf(50);

        Transaction expectedTransaction = Transaction.builder()
                .status(TransactionStatus.FAILED)
                .build();

        when(walletRepository.findById(wallet.getId())).thenReturn(Optional.of(wallet));
        when(transactionService.createNewTransaction(
                any(), anyString(), anyString(), any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(expectedTransaction);

        // When
        Transaction result = walletService.charge(user, wallet.getId(), amount, "Charge Payment");

        // Then
        assertEquals(TransactionStatus.FAILED, result.getStatus());
        assertEquals(BigDecimal.valueOf(20), wallet.getBalance());
        verify(walletRepository, never()).save(wallet);
        verify(eventPublisher, never()).publishEvent(any());
    }

    @Test
    void givenWalletWithInactiveStatus_whenCharge_thenReturnNewFailedTransaction() {

        // Given

        User user = User.builder()
                .id(UUID.randomUUID())
                .build();

        Wallet wallet = Wallet.builder()
                .id(UUID.randomUUID())
                .balance(BigDecimal.valueOf(100))
                .status(WalletStatus.INACTIVE)
                .owner(user)
                .build();

        BigDecimal amount = BigDecimal.valueOf(50);

        Transaction expectedTransaction = Transaction.builder()
                .status(TransactionStatus.FAILED)
                .build();

        when(walletRepository.findById(wallet.getId())).thenReturn(Optional.of(wallet));
        when(transactionService.createNewTransaction(
                any(), anyString(), anyString(), any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(expectedTransaction);

        // When
        Transaction result = walletService.charge(user, wallet.getId(), amount, "Charge Payment");

        // Then
        assertEquals(TransactionStatus.FAILED, result.getStatus());
        assertEquals(BigDecimal.valueOf(100), wallet.getBalance());
        verify(walletRepository, never()).save(any(Wallet.class));
        verify(eventPublisher, never()).publishEvent(any());
//        verify(transactionService).createNewTransaction(
//                user,
//                wallet.getId().toString(),
//                SIM_RACING_ACADEMY,
//                amount,
//                wallet.getBalance(),
//                wallet.getCurrency(),
//                TransactionType.WITHDRAWAL,
//                TransactionStatus.FAILED,
//                "Charge Payment",
//                "Inactive wallet status"
//        );
    }

    @Test
    void givenUserAndActiveWallet_whenTopUpWallet_thenAddMoney() {
        // Given

        User user = User.builder()
                .id(UUID.randomUUID())
                .build();

        Wallet wallet = Wallet.builder()
                .id(UUID.randomUUID())
                .balance(BigDecimal.valueOf(100))
                .status(WalletStatus.ACTIVE)
                .owner(user)
                .build();

        BigDecimal amount = BigDecimal.valueOf(50);

        Transaction expectedTransaction = new Transaction();
        expectedTransaction.setStatus(TransactionStatus.SUCCEEDED);

    when(walletRepository.findById(wallet.getId())).thenReturn(Optional.of(wallet));
    when(walletRepository.save(any(Wallet.class))).thenReturn(wallet);
    when(transactionService.createNewTransaction(
            any(), anyString(), anyString(), any(), any(), any(), any(), any(), any(), any()))
            .thenReturn(expectedTransaction);

    // When
    Transaction result = walletService.topUp(wallet.getId(), amount);

    // Then
    assertNotNull(result);
    assertEquals(TransactionStatus.SUCCEEDED, result.getStatus());
    assertEquals(BigDecimal.valueOf(150), wallet.getBalance());

    verify(walletRepository).save(wallet);
    verify(transactionService).createNewTransaction(
            eq(user), anyString(), eq(wallet.getId().toString()), eq(amount), eq(BigDecimal.valueOf(150)),
    eq(wallet.getCurrency()), eq(TransactionType.DEPOSIT), eq(TransactionStatus.SUCCEEDED),
    anyString(), isNull()
            );
    }

    @Test
    void givenUserAndInactiveWallet_whenTopUpWallet_thenReturnFailedTransaction() {
        // Given
        User user = User.builder()
                .id(UUID.randomUUID())
                .build();

        Wallet wallet = Wallet.builder()
                .id(UUID.randomUUID())
                .balance(BigDecimal.valueOf(100))
                .status(WalletStatus.INACTIVE)
                .owner(user)
                .build();

        BigDecimal amount = BigDecimal.valueOf(50);

        Transaction expectedTransaction = new Transaction();
        expectedTransaction.setStatus(TransactionStatus.FAILED);

        when(walletRepository.findById(wallet.getId())).thenReturn(Optional.of(wallet));
        when(transactionService.createNewTransaction(
                any(), anyString(), anyString(), any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(expectedTransaction);

        // When
        Transaction result = walletService.topUp(wallet.getId(), amount);

        // Then
        assertNotNull(result);
        assertEquals(TransactionStatus.FAILED, result.getStatus());
        assertEquals(BigDecimal.valueOf(100), wallet.getBalance()); // Balance should not change

        verify(walletRepository, never()).save(any(Wallet.class));
        verify(transactionService).createNewTransaction(
                eq(user), anyString(), eq(wallet.getId().toString()), eq(amount), eq(BigDecimal.valueOf(100)),
                eq(wallet.getCurrency()), eq(TransactionType.DEPOSIT), eq(TransactionStatus.FAILED),
                anyString(), eq("Inactive wallet")
        );
    }
}
