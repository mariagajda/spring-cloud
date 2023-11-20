package pl.training.payments.domain;

import lombok.extern.java.Log;
import pl.training.payments.ports.ShopService;

@Log
public class OrderProcessor implements ShopService {

    private static final String DEFAULT_CURRENCY = "PLN";

    @Override
    public void place(Order order) {
        log.info("New order with total value: %d %s".formatted(order.getTotalValue(), DEFAULT_CURRENCY));
    }

}
