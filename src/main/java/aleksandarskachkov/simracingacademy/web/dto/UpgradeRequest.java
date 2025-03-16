package aleksandarskachkov.simracingacademy.web.dto;

import aleksandarskachkov.simracingacademy.subscription.model.SubscriptionPeriod;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class UpgradeRequest {

    private UUID walletId;

    private SubscriptionPeriod subscriptionPeriod;
}
