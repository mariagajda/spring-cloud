package pl.training.payments.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

@Service
@RequiredArgsConstructor
public class ReactivePaymentService {

    private final PaymentRepository paymentRepository;
    private final Sinks.Many<Payment> paymentMany = Sinks.many().replay().all(10);

    public Flux<Payment> getAllPayments() {
        return paymentRepository.getAll();
    }

    public Mono<Payment> process(Mono<Payment> paymentMono) {
        return paymentMono
                .map(Payment::confirmed)
                .flatMap(paymentRepository::persist)
                .doOnNext(paymentMany::tryEmitNext);
    }

    public Flux<Payment> getProcessedPayments() {
        return paymentMany.asFlux();
    }

}
