package aleksandarskachkov.simracingacademy.web.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@Getter
@Setter
public class TransferRequest {

    @NotNull
    private UUID fromWalletId;

    @NotNull
    private String toUsername;

    @NotNull
    @Positive
    private BigDecimal amount;
}
