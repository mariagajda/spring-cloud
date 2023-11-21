package pl.training.payments;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import pl.training.payments.adapters.rest.ReactivePaymentServiceAdapter;
import pl.training.payments.adapters.websocket.WebSocketRatingSourceAdapter;

import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class PaymentsConfiguration {

    private static final String PAYMENTS = "payments";
    private static final String PROCESSED_PAYMENTS = PAYMENTS + "/processed";
    private static final String RATINGS = "ratings";

    @Bean
    public RouterFunction<ServerResponse> routes(ReactivePaymentServiceAdapter adapter) {
        return RouterFunctions
                .route(POST(PAYMENTS).and(accept(APPLICATION_JSON)), adapter::process)
                .andRoute(GET(PAYMENTS).and(accept(APPLICATION_JSON)), adapter::getAllPayments)
                .andRoute(GET(PROCESSED_PAYMENTS).and(accept(APPLICATION_JSON)), adapter::getAllProcessedPayments);
    }

    @Bean
    public HandlerMapping handlerMapping(WebSocketRatingSourceAdapter adapter) {
        var mapper = new SimpleUrlHandlerMapping();
        mapper.setOrder(1);
        mapper.setUrlMap(Map.of(RATINGS, adapter));
        return mapper;
    }

}
