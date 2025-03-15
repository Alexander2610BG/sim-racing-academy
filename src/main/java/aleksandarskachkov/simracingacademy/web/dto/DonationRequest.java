package aleksandarskachkov.simracingacademy.web.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class DonationRequest {

    @NotNull
    private UUID fromWalletId;

    @NotNull
    @Positive
    private BigDecimal amount;
}
