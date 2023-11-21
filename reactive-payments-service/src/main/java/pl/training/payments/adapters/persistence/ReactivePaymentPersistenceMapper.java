package pl.training.payments.adapters.persistence;

import org.mapstruct.Mapper;
import pl.training.payments.domain.Payment;

@Mapper(componentModel = "spring")
public interface ReactivePaymentPersistenceMapper {

    PaymentDocument toDocument(Payment payment);

    Payment toDomain(PaymentDocument paymentDocument);

}
