package pl.training.payments.adapters.events;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.training.payments.domain.PaymentUpdated;

@Mapper(componentModel = "spring")
public interface EventPaymentMapper {

    @Mapping(source = "paymentStatus", target = "status")
    PaymentEventDto toDto(PaymentUpdated event);

}
