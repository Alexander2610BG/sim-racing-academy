package aleksandarskachkov.simracingacademy.web;

import aleksandarskachkov.simracingacademy.security.AuthenticationMetadata;
import aleksandarskachkov.simracingacademy.transaction.model.Transaction;
import aleksandarskachkov.simracingacademy.user.model.User;
import aleksandarskachkov.simracingacademy.user.model.UserRole;
import aleksandarskachkov.simracingacademy.user.service.UserService;
import aleksandarskachkov.simracingacademy.wallet.model.Wallet;
import aleksandarskachkov.simracingacademy.wallet.service.WalletService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static aleksandarskachkov.simracingacademy.TestBuilder.aRandomTransaction;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@WebMvcTest(WalletController.class)
public class WalletControllerApiTest {

    @MockitoBean
    private WalletService walletService;
    @MockitoBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void putAuthorizedRequestToSwitchStatus_shouldRedirectToWallet() throws Exception {

        // 1. Build Request
        AuthenticationMetadata principal = new AuthenticationMetadata(UUID.randomUUID(), "User123", "123123", UserRole.ADMIN, true);
        MockHttpServletRequestBuilder request = put("/wallet/{id}/status", UUID.randomUUID())
                .with(user(principal))
                .with(csrf());

        // 2. Send Request
        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/wallet"));
    }

    @Test
    void putAuthorizedRequestToTopUp_shouldRedirectToTransaction() throws Exception {

        // 1. Build Request
        AuthenticationMetadata principal = new AuthenticationMetadata(UUID.randomUUID(), "User123", "123123", UserRole.ADMIN, true);
        when(walletService.topUp(UUID.randomUUID(), BigDecimal.valueOf(15))).thenReturn(aRandomTransaction());
        MockHttpServletRequestBuilder request = put("/wallet/{id}/balance/top-up", UUID.randomUUID())
                .with(user(principal))
                .with(csrf());

        // 2. Send Request
        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/transactions/" + UUID.randomUUID()));
    }

    @Test
    void getRequestToWalletEndpoint_shouldWalletView() throws Exception {

        // 1. Build Request


        AuthenticationMetadata principal = new AuthenticationMetadata(UUID.randomUUID(), "User123", "123123", UserRole.ADMIN, true);
        when(userService.getById(principal.getUserId()));
        when(walletService.getWalletByUser(principal.getUserId()));
        when(walletService.getLastFiveTransactions(Wallet.builder().owner(userService.getById(principal.getUserId())).build()));
        MockHttpServletRequestBuilder request = get("/wallet");

        // 2. Send Request

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("wallet"))
                .andExpect(model().attributeExists("wallet", "lastFiveTransactions", "user"));

    }

    @Test
    void getRequestToWalletEndpoint_shouldReturnWalletView() throws Exception {
        // Setup test data
        UUID userId = UUID.randomUUID();
        AuthenticationMetadata principal = new AuthenticationMetadata(userId, "User123", "123123", UserRole.ADMIN, true);

        User mockUser = User.builder()
                .id(userId)
                .wallet(Wallet.builder().id(UUID.randomUUID()).build())
                .build();

        Wallet mockWallet = Wallet.builder()
                .id(UUID.randomUUID())
                .owner(mockUser)
                .build();

        Map<UUID, List<Transaction>> mockTransactions = Map.of(
                mockWallet.getId(), List.of(
                        new Transaction(),
                        new Transaction()
                )
        );

        // Mock service responses
        when(userService.getById(userId)).thenReturn(mockUser);
        when(walletService.getWalletByUser(userId)).thenReturn(mockWallet);
        when(walletService.getLastFiveTransactions(mockUser.getWallet())).thenReturn(mockTransactions);

        // Perform request and verify
        mockMvc.perform(get("/wallet")
                        .with(user(principal)))
                .andExpect(status().isOk())
                .andExpect(view().name("wallet"))
                .andExpect(model().attributeExists("user", "wallet", "lastFiveTransactions"))
                .andExpect(model().attribute("wallet", mockWallet))
                .andExpect(model().attribute("lastFiveTransactions", mockTransactions));
    }
}
