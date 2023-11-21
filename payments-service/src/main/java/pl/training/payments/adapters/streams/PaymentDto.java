package pl.training.payments.adapters.streams;

import lombok.Data;

import java.time.Instant;

@Data
public class PaymentDto {

    private String id;
    private String value;
    private String currency;
    private Instant timestamp;
    private String status;

}
