package pl.training.payments.adapters.events;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;
import pl.training.payments.domain.PaymentUpdated;
import pl.training.payments.ports.PaymentsEventEmitter;

@Component
@RequiredArgsConstructor
public class PaymentEventPublisher implements PaymentsEventEmitter {

    private static final String EVENT_TYPE = "UPDATE";
    private static final String PAYMENTS_BINDING_NAME = "payments-out-0";

    private final StreamBridge streamBridge;
    private final EventPaymentMapper mapper;

    @Override
    public void emit(PaymentUpdated event) {
        var eventDto = mapper.toDto(event);
        eventDto.setType(EVENT_TYPE);
        streamBridge.send(PAYMENTS_BINDING_NAME, eventDto);
    }

}
