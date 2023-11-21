package pl.training.payments.adapters.streams;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;
import java.util.Optional;

@Component
@Log
@RequiredArgsConstructor
public class StreamClient {

    private static final String PAYMENTS_SERVICE = "REACTIVE-PAYMENTS-SERVICE";
    private static final String PAYMENTS_ENDPOINT = "/payments/processed";

    private final DiscoveryClient discoveryClient;

    @PostConstruct
    public void onStart() {
        getPaymentsUri().ifPresent(uri -> {
            WebClient.builder()
                    .build()
                    .get()
                    .uri(uri)
                    .retrieve()
                    .bodyToFlux(PaymentDto.class)
                    .map(PaymentDto::getStatus)
                    .subscribe(status -> log.info("New payment status: " + status), throwable -> log.warning("Exception: " + throwable));
        });
    }

    private Optional<URI> getPaymentsUri() {
        var instances = discoveryClient.getInstances(PAYMENTS_SERVICE);
        return instances.stream()
                .findFirst()
                .map(ServiceInstance::getUri)
                .map(uri -> uri.resolve(PAYMENTS_ENDPOINT));
    }

}
