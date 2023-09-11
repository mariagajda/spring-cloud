package pl.training.gateway;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.Collections.singletonList;

@Component
public class RequestLoggerGatewayFilterFactory extends AbstractGatewayFilterFactory<RequestLoggerGatewayFilterFactory.Config> {

    private static final Logger LOGGER = Logger.getLogger(RequestLoggerGatewayFilterFactory.class.getName());

    public RequestLoggerGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            LOGGER.log(config.level, "New request for path: %s".formatted(exchange.getRequest().getPath()));
            ServerHttpRequest.Builder builder = exchange.getRequest().mutate();
            return chain.filter(exchange.mutate().request(builder.build()).build());
        };
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return singletonList("level");
    }

    public static class Config {

        private Level level;

        public void setLevel(String level) {
            this.level = level.equals("info") ? Level.INFO : Level.OFF;
        }

    }

}

