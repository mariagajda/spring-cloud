package pl.training.payments.adapters.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import pl.training.payments.domain.ReactivePaymentService;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ReactivePaymentServiceAdapter {

    private final ReactivePaymentService paymentService;
    private final RestPaymentMapper mapper;

    public Mono<ServerResponse> process(ServerRequest serverRequest) {
        var payment = serverRequest.bodyToMono(PaymentDto.class).map(mapper::toDomain);
        var paymentDtoMono = paymentService.process(payment).map(mapper::toDto);
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(paymentDtoMono, PaymentDto.class);
    }

    public Mono<ServerResponse> getAllPayments(ServerRequest serverRequest) {
        var paymentsFlux = paymentService.getAllPayments().map(mapper::toDto);
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(paymentsFlux, PaymentDto.class);
    }

    public Mono<ServerResponse> getProcessedPayments(ServerRequest serverRequest) {
        var paymentsFlux = paymentService.getProcessedPayments().map(mapper::toDto);
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(paymentsFlux, PaymentDto.class);
    }

}
