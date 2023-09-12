package pl.training.payments.adapters.rest;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("PAYMENTS-SERVICE")
public interface PaymentsApi {
}
