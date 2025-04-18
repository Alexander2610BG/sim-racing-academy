package aleksandarskachkov.simracingacademy.notification.service;

import aleksandarskachkov.simracingacademy.exception.NotificationPreferenceDoesntExist;
import aleksandarskachkov.simracingacademy.exception.NotificationServiceFeignCallException;
import aleksandarskachkov.simracingacademy.notification.client.NotificationClient;
import aleksandarskachkov.simracingacademy.notification.client.dto.Notification;
import aleksandarskachkov.simracingacademy.notification.client.dto.NotificationPreference;
import aleksandarskachkov.simracingacademy.notification.client.dto.NotificationRequest;
import aleksandarskachkov.simracingacademy.notification.client.dto.UpsertNotificationPreference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class NotificationService {

    @Value("${notification-svc.failure-message.clear.history}")
    private String clearHistoryFailedMessage;

    private final NotificationClient notificationClient;

    @Autowired
    public NotificationService(NotificationClient notificationClient) {
        this.notificationClient = notificationClient;
    }

    public NotificationPreference getNotificationPreference(UUID userId) {

        ResponseEntity<NotificationPreference> httpResponse = notificationClient.getUserPreference(userId);

        if (!httpResponse.getStatusCode().is2xxSuccessful()) {
            throw new NotificationPreferenceDoesntExist("Notification preference for user id [%s] does not exists.".formatted(userId));
        }

        return httpResponse.getBody();
    }

    public List<Notification> getNotificationHistory(UUID userId) {

        ResponseEntity<List<Notification>> httpResponse = notificationClient.getNotificationHistory(userId);

        return httpResponse.getBody();
    }

    public void saveNotificationPreference(UUID userId, boolean isEmailEnable, String email) {

        UpsertNotificationPreference notificationPreference = UpsertNotificationPreference.builder()
                .userId(userId)
                .contactInfo(email)
                .type("EMAIL")
                .notificationEnabled(isEmailEnable)
                .build();

        try {
            ResponseEntity<Void> httpResponse = notificationClient.upsertNotificationPreference(notificationPreference);
            if (!httpResponse.getStatusCode().is2xxSuccessful()) {
                log.error("[Feign call to notification-svc failed] Can't save user preference for user with id = [%s]".formatted(userId));
            }
        } catch (Exception e) {
            log.error("Unable to call notification-svc.");
        }
    }

    public void updateNotificationPreference(UUID userId, boolean enabled) {

        notificationClient.updateNotificationPreference(userId, enabled);
    }

    public void sendNotification(UUID userId, String emailSubject, String emailBody) {

        NotificationRequest notificationRequest = NotificationRequest.builder()
                .userId(userId)
                .subject(emailSubject)
                .body(emailBody)
                .build();

        ResponseEntity<Void> httpResponse;
        try {
            httpResponse = notificationClient.sendNotification(notificationRequest);
            if (!httpResponse.getStatusCode().is2xxSuccessful()) {
                log.error("[Feign call to notification-svc failed] Can't send email to user with id = [%s]".formatted(userId));
            }
        } catch (Exception e) {
            log.warn("Can't send email to user with id = [%s] due to Internal Server Error".formatted(userId));
        }
    }

    public void clearHistory(UUID userId) {

        try {
            notificationClient.clearHistory(userId);
        } catch (Exception e) {
            throw new NotificationServiceFeignCallException(clearHistoryFailedMessage);
        }
    }

    public void retryFailed(UUID userId) {

        try {
            notificationClient.retryFailedNotification(userId);
        } catch (Exception e) {
            log.error("Unable to call notification-svc for clear notification history.");
            throw new NotificationServiceFeignCallException(clearHistoryFailedMessage);
        }
    }
}
