package apigateway.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;



@Component
public class AuthenticationFilter
        extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    private final WebClient.Builder webClientBuilder;

    private static final Logger log = LoggerFactory.getLogger(AuthenticationFilter.class);


    public AuthenticationFilter(WebClient.Builder webClientBuilder) {
        super(Config.class);
        this.webClientBuilder = webClientBuilder;
    }

    @Value("${authentication.url}")
    private String url;

    @Value("${gateway.secret}")
    private String gatewaySecret;

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {

            log.info("Received request for path: {}", exchange);

            String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                log.warn("Missing or invalid Authorization header for path: {}",
                        exchange.getRequest().getPath());
                return exchange.getResponse().setComplete();
            }

            return webClientBuilder.build()
                    .post()
                    .uri(url)
                    .header(HttpHeaders.AUTHORIZATION, authHeader)
                    .retrieve()
                    .bodyToMono(ValidationResponse.class)
                    .flatMap(response -> {
                        if (!response.valid()) {
                            log.error("Authentication failed for user: {} with message: {}",
                                    response.userInfo().email(), response.message());
                            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                            return exchange.getResponse().setComplete();
                        }

                        ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                                .header("X-User-Id", response.userInfo().userId())
                                .header("X-User-Email", response.userInfo().email())
                                .header("X-User-Role", response.userInfo().role().name())
                                .header("X-Gateway-Secret", gatewaySecret)
                                .build();

                        return chain.filter(exchange.mutate().request(modifiedRequest).build());
                    })
                    .doOnError(error -> log.error("Error during authentication: {}", error.getMessage()));
        };
    }


    public static class Config {}
}
