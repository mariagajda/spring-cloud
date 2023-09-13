package pl.training.payments.domain;

import lombok.Builder;
import lombok.Value;
import lombok.With;
import org.javamoney.moneta.Money;

import java.time.Instant;

import static pl.training.payments.domain.PaymentStatus.CONFIRMED;

@Builder
@Value
public class Payment {

    String id;
    Money value;
    Instant timestamp;
    @With
    PaymentStatus status;

    public boolean isConfirmed() {
        return status == CONFIRMED;
    }

    public Payment confirmed() {
        return withStatus(CONFIRMED);
    }

}
