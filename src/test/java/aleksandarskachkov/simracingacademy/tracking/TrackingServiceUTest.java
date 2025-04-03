package aleksandarskachkov.simracingacademy.tracking;

import aleksandarskachkov.simracingacademy.tracking.service.TrackingService;
import aleksandarskachkov.simracingacademy.web.dto.PaymentNotificationEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TrackingServiceUTest {

    @Mock
    private PaymentNotificationEvent event;


    @InjectMocks
    private TrackingService trackingService;

    @Test
    void givenPaymentNotificationEvent_whenTrackNewPayment_thenLogsEvent() {

        // Given

        UUID userId = UUID.randomUUID();
        when(event.getUserId()).thenReturn(userId);

        // When
        trackingService.trackNewPayment(event);

        // Then
        verify(event).getUserId();
    }
}
