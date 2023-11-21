package pl.training.payments.adapters.persistence;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ReactivePaymentRepository extends ReactiveMongoRepository<PaymentDocument, String> {
}
