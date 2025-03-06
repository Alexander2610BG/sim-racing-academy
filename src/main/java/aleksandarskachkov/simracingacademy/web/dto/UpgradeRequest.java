package aleksandarskachkov.simracingacademy.web.dto;

import aleksandarskachkov.simracingacademy.subscription.model.SubscriptionPeriod;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpgradeRequest {

    private SubscriptionPeriod subscriptionPeriod;
}
