package pl.training.payments.adapters.payments.stream;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import pl.training.payments.adapters.payments.PaymentDto;

import java.net.URI;

@Log
@Component
@RequiredArgsConstructor
public class StreamPaymentAdapter {

    private static final String PAYMENTS_SERVICE = "REACTIVE-PAYMENTS-SERVICE";
    private static final String PAYMENTS_ENDPOINT = "/payments/processed";

    private final DiscoveryClient discoveryClient;

    //@PostConstruct
    public void init() {
        WebClient.builder()
                .build()
                .get()
                .uri(getPaymentsUri())
                .retrieve()
                .bodyToFlux(PaymentDto.class)
                .map(PaymentDto::getStatus)
                .subscribe(paymentDto -> log.info("New Payment event: " + paymentDto), throwable -> log.info("Exception: " + throwable));
    }

    private URI getPaymentsUri() {
        var instances = discoveryClient.getInstances(PAYMENTS_SERVICE);
        var instance = instances.stream()
                .findFirst()
                .orElseThrow(IllegalStateException::new);
        return instance.getUri().resolve(PAYMENTS_ENDPOINT);
    }

}
