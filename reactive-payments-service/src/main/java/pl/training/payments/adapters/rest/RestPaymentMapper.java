package pl.training.payments.adapters.rest;

import org.mapstruct.Mapper;
import pl.training.payments.commons.ResultPage;
import pl.training.payments.commons.data.MoneyMapper;
import pl.training.payments.commons.web.ResultPageDto;
import pl.training.payments.domain.Payment;

@Mapper(componentModel = "spring", uses = MoneyMapper.class)
public interface RestPaymentMapper {

    Payment toDomain(PaymentDto paymentDto);

    PaymentDto toDto(Payment payment);

    ResultPageDto<PaymentDto> toDto(ResultPage<Payment> paymentDomainResultPage);

}
