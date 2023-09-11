package pl.training.payments.adapters.payments;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import pl.training.payments.ports.PaymentsService;

@Component
@RequiredArgsConstructor
public class RestTemplatePaymentsService implements PaymentsService {

    private final RestTemplate restTemplate;

    @Override
    public boolean pay(long amount, String currency) {
        return false;
    }

}
