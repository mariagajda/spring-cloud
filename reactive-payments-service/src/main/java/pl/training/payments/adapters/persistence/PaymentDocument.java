package pl.training.payments.adapters.persistence;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;

@Document(collation = "payments")
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class PaymentDocument {

    @Id
    private String id;
    private BigDecimal value;
    private String currency;
    private Instant timestamp;
    private String status;

}
