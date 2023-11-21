package pl.training.payments.adapters.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import pl.training.payments.domain.ReactivePaymentService;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
@RequiredArgsConstructor
public class ReactivePaymentServiceAdapter {

    private final ReactivePaymentService paymentService;
    private final RestPaymentMapper mapper;

    public Mono<ServerResponse> getAllPayments(ServerRequest request) {
        var paymentDtosFlux = paymentService.getAllPayments().map(mapper::toDto);
        return ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(paymentDtosFlux, PaymentDto.class);
    }

    public Mono<ServerResponse> getAllProcessedPayments(ServerRequest request) {
        var paymentDtosFlux = paymentService.getProcessedPayments().map(mapper::toDto);
        return ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(paymentDtosFlux, PaymentDto.class);
    }

    public Mono<ServerResponse> process(ServerRequest request) {
        var payment = request.bodyToMono(PaymentDto.class).map(mapper::toDomain);
        var paymentDtoMono = paymentService.process(payment).map(mapper::toDto);
        return ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(paymentDtoMono, PaymentDto.class);
    }

}
