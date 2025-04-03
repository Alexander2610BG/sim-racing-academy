package aleksandarskachkov.simracingacademy.subscription;

import aleksandarskachkov.simracingacademy.module.repository.ModuleRepository;
import aleksandarskachkov.simracingacademy.subscription.model.Subscription;
import aleksandarskachkov.simracingacademy.subscription.repository.SubscriptionRepository;
import aleksandarskachkov.simracingacademy.subscription.service.SubscriptionService;
import aleksandarskachkov.simracingacademy.track.service.TrackService;
import aleksandarskachkov.simracingacademy.user.model.User;
import aleksandarskachkov.simracingacademy.wallet.service.WalletService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

//import static jdk.internal.org.objectweb.asm.util.CheckClassAdapter.verify;
import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SubscriptionServiceUTest {

    @Mock
    private SubscriptionRepository subscriptionRepository;
    @Mock
    private TrackService trackService;
    @Mock
    private WalletService walletService;
    @Mock
    private ModuleRepository moduleRepository;

    @InjectMocks
    private SubscriptionService subscriptionService;

//    @Test
//    void createDefaultSubscription_shouldCreateAndReturnSubscription() {
//        // Given
//        User testUser = new User("testUser");
//        Subscription expectedSubscription = new Subscription(testUser, SubscriptionType.DEFAULT);
//
//        when(subscriptionRepository.save(any(Subscription.class)))
//                .thenReturn(expectedSubscription);
//
//        // When
//        Subscription result = subscriptionService.createDefaultSubscription(testUser);
//
//        // Then
//        assertNotNull(result);
//        assertEquals(expectedSubscription, result);
//        verify(subscriptionRepository, times(1)).save(any(Subscription.class));
//        // Verify log message would require a different approach as it's static
//    }
}
