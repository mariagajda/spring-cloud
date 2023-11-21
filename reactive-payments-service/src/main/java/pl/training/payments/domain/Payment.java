package pl.training.payments.domain;

import lombok.Builder;
import lombok.Data;
import lombok.Value;
import lombok.With;

import java.math.BigDecimal;
import java.time.Instant;

@Builder
@Value
public class Payment {

    private static final String CONFIREMD = "CONFIREMD";

    String id;
    BigDecimal value;
    String currency;
    Instant timestamp;
    @With
    String status;

    public boolean isConfirmed() {
        return CONFIREMD.equals(status);
    }

    public Payment confirmed() {
        return withStatus(CONFIREMD);
    }

}
