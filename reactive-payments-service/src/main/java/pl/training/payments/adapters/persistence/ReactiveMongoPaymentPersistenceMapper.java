package pl.training.payments.adapters.persistence;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;
import pl.training.payments.commons.ResultPage;
import pl.training.payments.domain.Payment;
import pl.training.payments.domain.PaymentStatus;

import java.util.List;

@Mapper(componentModel = "spring", imports = {java.math.BigDecimal.class, org.javamoney.moneta.Money.class})
public interface ReactiveMongoPaymentPersistenceMapper {

    @Mapping(target = "value", expression = "java(BigDecimal.valueOf(payment.getValue().getNumber().doubleValueExact()))")
    @Mapping(target = "currency", expression = "java(payment.getValue().getCurrency().getCurrencyCode())")
    PaymentDocument toDocument(Payment payment);

    @Mapping(target = "value", expression = "java(Money.of(paymentDocument.getValue(), paymentDocument.getCurrency()))")
    Payment toDomain(PaymentDocument paymentDocument);

    String toDocument(PaymentStatus paymentStatus);

    @IterableMapping(elementTargetType = Payment.class)
    List<Payment> toDomain(List<PaymentDocument> paymentEntities);

    @Mapping(source = "content", target = "data")
    @Mapping(source = "number", target = "pageNumber")
    ResultPage<Payment> toDomain(Page<PaymentDocument> paymentEntityPage);

}
