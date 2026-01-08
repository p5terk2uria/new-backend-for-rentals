package apigateway.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    private final WebClient.Builder webClientBuilder;

    public AuthenticationFilter(WebClient.Builder webClientBuilder) {
        super(Config.class);
        this.webClientBuilder = webClientBuilder;
    }

    @Value("${authentication.url}")
    private String url;

    @Value("${gateway.secret}")
    private  String gatewaySecret;

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                return onError(exchange, "Missing authorization header", HttpStatus.UNAUTHORIZED);
            }

            String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

            return webClientBuilder.build()
                    .post()
                    .uri(url)
                    .header(HttpHeaders.AUTHORIZATION, authHeader)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .onStatus(
                            status -> status.value() == 401,
                            response -> Mono.error(new RuntimeException("Unauthorized"))
                    )
                    .bodyToMono(ValidationResponse.class)
                    .flatMap(validationResponse -> {
                        if (validationResponse.valid()) {

                            ServerHttpRequest modifiedRequest = exchange.getRequest()
                                    .mutate()
                                    .header("X-User-Id", validationResponse.userInfo().userId())
                                    .header("X-User-Email", validationResponse.userInfo().email())
                                    .header("X-User-Role", String.valueOf(validationResponse.userInfo().role()))
                                    .header("X-Gateway-Secret",gatewaySecret)
                                    .build();
                            return chain.filter(exchange.mutate().request(modifiedRequest).build());
                        } else {
                            return onError(exchange, validationResponse.message(), HttpStatus.UNAUTHORIZED);
                        }
                    }).onErrorResume(error -> {
                        return onError(exchange, "Authentication failed", HttpStatus.UNAUTHORIZED);
                    });

        };

    }


    private Mono<Void> onError(ServerWebExchange exchange, String message, HttpStatus status) {
        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().add("Content-Type", "application/json");

        String errorResponse = String.format("{\"error\": \"%s\", \"status\": %d}", message, status.value());

        return exchange.getResponse()
                .writeWith(Mono.just(exchange.getResponse()
                        .bufferFactory()
                        .wrap(errorResponse.getBytes())));
    }

    public static class Config {
    }
}
