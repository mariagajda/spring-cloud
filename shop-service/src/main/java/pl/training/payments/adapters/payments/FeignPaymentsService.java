
package pl.training.payments.adapters.payments;

import feign.FeignException.FeignClientException;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import pl.training.payments.ports.PaymentsService;

import java.util.Optional;

@Primary
@Component
@Log
@RequiredArgsConstructor
public class FeignPaymentsService implements PaymentsService {

    private final PaymentsApi paymentsApi;

    @Retry(name = "payments", fallbackMethod = "payFallback")
    @Override
    public boolean pay(long amount, String currency) {
        var paymentRequestDto = new PaymentRequestDto();
        paymentRequestDto.setValue("%d %s".formatted(amount, currency));
       // try {
            var paymentDto = paymentsApi.pay(paymentRequestDto);
            return Optional.ofNullable(paymentDto)
                    .map(payment -> payment.status.equals("STARTED"))
                    .orElse(false);
//        } catch (FeignClientException restClientException) {
//            log.info("Payment failed: " + restClientException.getMessage());
//        }
//        return false;
    }

    public boolean payFallback(long amount, String currency, RuntimeException runtimeException) {
        log.info("######");
        return false;
    }

}
