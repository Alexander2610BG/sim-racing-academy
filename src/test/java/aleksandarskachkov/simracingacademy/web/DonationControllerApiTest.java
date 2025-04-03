package aleksandarskachkov.simracingacademy.web;

import aleksandarskachkov.simracingacademy.security.AuthenticationMetadata;
import aleksandarskachkov.simracingacademy.user.model.UserRole;
import aleksandarskachkov.simracingacademy.user.service.UserService;
import aleksandarskachkov.simracingacademy.wallet.service.WalletService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.UUID;

import static aleksandarskachkov.simracingacademy.TestBuilder.aRandomTransaction;
import static aleksandarskachkov.simracingacademy.TestBuilder.aRandomUser;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DonationController.class)
public class DonationControllerApiTest {

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private WalletService walletService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getDonationPage_shouldReturnDonationPage() throws Exception {
        when(userService.getById(any())).thenReturn(aRandomUser());

        UUID userId = UUID.randomUUID();
        AuthenticationMetadata principal = new AuthenticationMetadata(userId, "User123", "123123", UserRole.ADMIN, true);

        MockHttpServletRequestBuilder request = get("/donations")
                .with(user(principal));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("donations"))
                .andExpect(model().attributeExists("donationRequest"));
    }

    @Test
    void postInitiateDonationWithInvalidData_returnRegisterView() throws Exception {

        // 1. Build Request
        when(userService.getById(any())).thenReturn(aRandomUser());

        UUID userId = UUID.randomUUID();
        AuthenticationMetadata principal = new AuthenticationMetadata(userId, "User123", "123123", UserRole.ADMIN, true);

        MockHttpServletRequestBuilder request = post("/donations")
                .with(user(principal))
                .formField("fromWalletId", "")
                .formField("amount", "")
                .with(csrf());

        // 2. Send Request
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("donations"))
                .andExpect(model().attributeExists("donationRequest", "user"));

        verify(walletService, never()).donate(any(), any());

    }

//    @Test
//    void postInitiateDonationWithValidData_returnTransactionView() throws Exception {
//        when(userService.getById(any())).thenReturn(aRandomUser());
//
//        UUID userId = UUID.randomUUID();
//        AuthenticationMetadata principal = new AuthenticationMetadata(userId, "User123", "123123", UserRole.ADMIN, true);
//
//        MockHttpServletRequestBuilder request = post("/donations")
//                .with(user(principal))
//                .formField("fromWalletId", )
//                .formField("amount", "1.00")
//                .with(csrf());
//
//        mockMvc.perform(request)
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/transactions/" + aRandomTransaction().getId()));
//
//        verify(walletService, times(1)).donate(any(), any());
//    }

    @Test
    void getAuthenticatedRequestToHome_returnsHomeView() throws Exception {

        when(userService.getById(any())).thenReturn(aRandomUser());

        UUID userId = UUID.randomUUID();
        AuthenticationMetadata principal = new AuthenticationMetadata(userId, "Alex123", "Test123#", UserRole.USER, true);

        MockHttpServletRequestBuilder request = get("/home")
                .with(user(principal));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(model().attributeExists("user"));
        verify(userService, times(1)).getById(userId);
    }
}
