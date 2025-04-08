package aleksandarskachkov.simracingacademy.web;

import aleksandarskachkov.simracingacademy.exception.UsernameAlreadyExist;
import aleksandarskachkov.simracingacademy.security.AuthenticationMetadata;
import aleksandarskachkov.simracingacademy.subscription.model.SubscriptionPeriod;
import aleksandarskachkov.simracingacademy.subscription.model.SubscriptionType;
import aleksandarskachkov.simracingacademy.subscription.service.SubscriptionService;
import aleksandarskachkov.simracingacademy.transaction.model.Transaction;
import aleksandarskachkov.simracingacademy.user.model.User;
import aleksandarskachkov.simracingacademy.user.model.UserRole;
import aleksandarskachkov.simracingacademy.user.service.UserService;
import aleksandarskachkov.simracingacademy.web.dto.UpgradeRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.UUID;

import static aleksandarskachkov.simracingacademy.TestBuilder.aRandomUser;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SubscriptionController.class)
public class SubscriptionControllerApiTest {

    @MockitoBean
    private UserService userService;
    @MockitoBean
    private SubscriptionService subscriptionService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getUpgradePage_thenReturnUpgradePage() throws Exception {

        when(userService.getById(any())).thenReturn(aRandomUser());

        UUID userId = UUID.randomUUID();
        AuthenticationMetadata principal = new AuthenticationMetadata(userId, "User123", "123123", UserRole.ADMIN, true);

        MockHttpServletRequestBuilder request = get("/subscriptions")
                .with(user(principal));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("upgrade"))
                .andExpect(model().attributeExists("upgradeRequest", "user"));
    }

    @Test
    void getHistoryPage_thenReturnHistoryPage() throws Exception {

        when(userService.getById(any())).thenReturn(aRandomUser());

        UUID userId = UUID.randomUUID();
        AuthenticationMetadata principal = new AuthenticationMetadata(userId, "User123", "123123", UserRole.ADMIN, true);

        MockHttpServletRequestBuilder request = get("/subscriptions/history")
                .with(user(principal));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("subscription-history"))
                .andExpect(model().attributeExists( "user"));
    }

    @Test
    void postRequestToRegisterEndpointWhenUsernameAlreadyExist_thenRedirectToRegisterWithFlashParameter() throws Exception {

        // 1. Build Request
        when(userService.getById(any())).thenReturn(aRandomUser());
        when(subscriptionService.upgrade(aRandomUser(), SubscriptionType.PREMIUM, UpgradeRequest.builder().build()));

        UUID userId = UUID.randomUUID();
        AuthenticationMetadata principal = new AuthenticationMetadata(userId, "User123", "123123", UserRole.ADMIN, true);

        MockHttpServletRequestBuilder request = get("/subscriptions")
                .with(user(principal));


        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/transactions/" + any()));
    }

    @Test
    void postUpgrade_thenRedirectToTransactionDetails() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID walletId = UUID.randomUUID();
        AuthenticationMetadata principal = new AuthenticationMetadata(userId, "User123", "123123", UserRole.ADMIN, true);

        User user = User.builder()
                .id(userId)
                .username("User123")
                .role(UserRole.USER)
                .build();

        Transaction transaction = Transaction.builder()
                .id(UUID.randomUUID())
                .build();

        when(userService.getById(userId)).thenReturn(user);
        when(subscriptionService.upgrade(eq(user), eq(SubscriptionType.PREMIUM), any(UpgradeRequest.class)))
                .thenReturn(transaction);

        mockMvc.perform(post("/subscriptions")
                        .param("subscription-type", "PREMIUM")
                        .param("walletId", walletId.toString())
                        .with(user(principal))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/transactions/" + transaction.getId()));

        verify(subscriptionService).upgrade(eq(user), eq(SubscriptionType.PREMIUM), any(UpgradeRequest.class));
    }
}
