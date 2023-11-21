package pl.training.payments.adapters.events;

import org.mapstruct.Mapper;
import pl.training.payments.domain.PaymentUpdated;

@Mapper(componentModel = "spring")
public interface EventPaymentMapper {

    PaymentEventDto toDto(PaymentUpdated event);

}
