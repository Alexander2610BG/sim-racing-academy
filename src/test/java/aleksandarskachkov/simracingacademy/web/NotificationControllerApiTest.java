package aleksandarskachkov.simracingacademy.web;

import aleksandarskachkov.simracingacademy.notification.client.dto.Notification;
import aleksandarskachkov.simracingacademy.notification.client.dto.NotificationPreference;
import aleksandarskachkov.simracingacademy.notification.service.NotificationService;
import aleksandarskachkov.simracingacademy.security.AuthenticationMetadata;
import aleksandarskachkov.simracingacademy.transaction.model.Transaction;
import aleksandarskachkov.simracingacademy.user.model.User;
import aleksandarskachkov.simracingacademy.user.model.UserRole;
import aleksandarskachkov.simracingacademy.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NotificationController.class)
public class NotificationControllerApiTest {

    @MockitoBean
    private UserService userService;
    @MockitoBean
    private NotificationService notificationService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void putRetryFailedNotification_thenRedirectToNotificationsAndReturnNotifications() throws Exception {

        UUID userId = UUID.randomUUID();
        AuthenticationMetadata principal = new AuthenticationMetadata(userId, "User123", "123123", UserRole.ADMIN, true);

        MockHttpServletRequestBuilder request = put("/notifications")
                .with(user(principal))
                .with(csrf());


        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/notifications"));

        verify(notificationService).retryFailed(userId);
    }

    @Test
    void putUpdateUserPreference_thenRedirectToNotificationsAndUpdateNotificationPreference() throws Exception {

        UUID userId = UUID.randomUUID();
        AuthenticationMetadata principal = new AuthenticationMetadata(userId, "User123", "123123", UserRole.ADMIN, true);

        MockHttpServletRequestBuilder request = put("/notifications/user-preference")
                .param("enabled", "true")
                .with(user(principal))
                .with(csrf());


        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/notifications"));

        verify(notificationService).updateNotificationPreference(userId, true);
    }

    @Test
    void deleteNotificationHistory_thenRedirectAndClearHistory() throws Exception {

        UUID userId = UUID.randomUUID();
        AuthenticationMetadata principal = new AuthenticationMetadata(userId, "User123", "123123", UserRole.ADMIN, true);

        MockHttpServletRequestBuilder request = delete("/notifications")
                .with(user(principal))
                .with(csrf());


        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/notifications"));

        verify(notificationService).clearHistory(userId);
    }

    @Test
    void getNotificationsPage_thenReturnsNotificationsViewWithCorrectModel() throws Exception {
        UUID userId = UUID.randomUUID();
        AuthenticationMetadata principal = new AuthenticationMetadata(userId, "User123", "123123", UserRole.ADMIN, true);


        User user = User.builder()
                .id(userId)
                .username("User123")
                .role(UserRole.ADMIN)
                .build();

        NotificationPreference preference = NotificationPreference.builder()
                .enabled(true)
                .build();

        List<Notification> history = List.of(
                Notification.builder().status("SUCCEEDED").build(),
                Notification.builder().status("FAILED").build()
        );

        when(userService.getById(userId)).thenReturn(user);
        when(notificationService.getNotificationPreference(userId)).thenReturn(preference);
        when(notificationService.getNotificationHistory(userId)).thenReturn(history);

        mockMvc.perform(get("/notifications")
                        .with(user(principal))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("notifications"))
                .andExpect(model().attribute("notificationPreference", preference))
                .andExpect(model().attribute("notificationHistory", history))
                .andExpect(model().attribute("succeededNotificationsNumber", 1L))
                .andExpect(model().attribute("failedNotificationsNumber", 1L))
                .andExpect(model().attribute("user", user));
    }

}
