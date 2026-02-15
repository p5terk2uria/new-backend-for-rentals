package productservice.pesapal;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import productservice.bookings.RegisterIPNRequest;
import productservice.bookings.dto.*;
import productservice.externalApIs.PesaPalConfigurations;
import productservice.payment.dto.InitiatePaymentResponse;


import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class PesaPal {

    private final RestTemplate restTemplate;
    private final PesaPalConfigurations pesaPalConfigurations;

    public AuthenticationResponse requestToken() {

        PesaPalAuthentication request = new PesaPalAuthentication(
                pesaPalConfigurations.getConsumerKey(),
                pesaPalConfigurations.getConsumerSecret()
        );

        log.info("::::::::: sending request with keys {}", request);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<PesaPalAuthentication> entity = new HttpEntity<>(request, headers);

        ResponseEntity<AuthenticationResponse> response = restTemplate.exchange(
                pesaPalConfigurations.getLiveAuthenticationUrl(),
                HttpMethod.POST,
                entity,
                AuthenticationResponse.class);
        assert response.getBody() != null;
        log.info("RAW RESPONSE: {}", response.getBody());

        return response.getBody();
    }

    public InitiatePaymentResponse submitOrderRequest(SubmitRequest request) {

        AuthenticationResponse auth = requestToken();

        if (auth == null || auth.token() == null) {
            throw new RuntimeException("Failed to get pesaPal token");
        }

        log.info("Submitting order with token: {}", auth.token());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(auth.token());

        HttpEntity<SubmitRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<SubmitResponse> response = restTemplate.exchange(
                pesaPalConfigurations.getSubmitRequestUrl(),
                HttpMethod.POST,
                entity,
                SubmitResponse.class
        );

        log.info("PesaPal SubmitOrderRequest response: {}", response.getBody());

        SubmitResponse body = response.getBody();
        if (body == null) {
            log.error("Pesapal returned null response");
            return null;
        }

        log.info("PesaPal SubmitOrderRequest response: {}", body);

        return InitiatePaymentResponse.builder()
                .orderTrackingId(body.orderTrackingId())
                .merchantReference(body.merchantReference())
                .redirectUrl(body.redirectUrl())
                .message(body.message())
                .build();

    }

    public RegisterIPNResponse registerIPN() {

        AuthenticationResponse auth = requestToken();
        if (auth == null || auth.token() == null) {
            throw new RuntimeException("Failed to get PesaPal token");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(auth.token());

        RegisterIPNRequest request = new RegisterIPNRequest(pesaPalConfigurations.getIpnUrl(), "POST");

        HttpEntity<RegisterIPNRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<RegisterIPNResponse> response = restTemplate.exchange(
                pesaPalConfigurations.getIpnRegistrationUrl(),
                HttpMethod.POST,
                entity,
                RegisterIPNResponse.class
        );

        log.info("IPN Registration response: {}", response.getBody());

        return response.getBody();
    }

}
