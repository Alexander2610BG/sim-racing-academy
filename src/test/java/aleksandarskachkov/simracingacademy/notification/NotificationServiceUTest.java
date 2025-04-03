package aleksandarskachkov.simracingacademy.notification;

import aleksandarskachkov.simracingacademy.notification.client.NotificationClient;
import aleksandarskachkov.simracingacademy.notification.client.dto.NotificationPreference;
import aleksandarskachkov.simracingacademy.notification.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceUTest {

    @Mock
    private String clearHistoryFailedMessage;
    @Mock
    private  NotificationClient notificationClient;

    @InjectMocks
    private NotificationService notificationService;

    @Test
    void givenNotificationPreference_whenGetNotificationPreference_thenExpectException() {


        // Given
        UUID userId = UUID.randomUUID();
        ResponseEntity<NotificationPreference> httpResponse = new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

        when(notificationClient.getUserPreference(userId)).thenReturn(httpResponse);

        // When & Then
        assertThrows(RuntimeException.class, () -> notificationService.getNotificationPreference(userId));
    }

    @Test
    void shouldReturnNotificationPreferenceWhenResponseIsSuccessful() {
        // Given
        UUID userId = UUID.randomUUID();
        NotificationPreference expectedPreference = NotificationPreference.builder()
                .type("EMAIL")
                .contactInfo("alex@gmail.com")
                .enabled(true)
                .build();
        ResponseEntity<NotificationPreference> httpResponse = new ResponseEntity<>(expectedPreference, HttpStatus.OK);

        when(notificationClient.getUserPreference(userId)).thenReturn(httpResponse);

        // When
        NotificationPreference result = notificationService.getNotificationPreference(userId);

        // Then
        assertNotNull(result);
//        assertEquals(expectedPreference.getUserId(), result.getUserId());
        assertEquals(expectedPreference.getContactInfo(), result.getContactInfo());
        assertEquals(expectedPreference.isEnabled(), result.isEnabled());
        assertEquals(expectedPreference.getType(), result.getType());
    }

}
