package pl.training.payments.ports;

import pl.training.payments.domain.PaymentUpdated;

public interface PaymentsEventEmitter {

    void emit(PaymentUpdated event);

}
