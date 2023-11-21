package pl.training.payments.adapters.events;

import lombok.extern.java.Log;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Log
@Component
public class PaymentEventConsumer implements Consumer<PaymentEventDto> {

    @Override
    public void accept(PaymentEventDto paymentEventDto) {
        log.info("New payment update: " + paymentEventDto);
    }

}
