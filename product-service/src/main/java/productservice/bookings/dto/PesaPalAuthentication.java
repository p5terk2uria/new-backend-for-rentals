package productservice.bookings.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PesaPalAuthentication(
        @JsonProperty("consumer_key") String consumerKey,
        @JsonProperty("consumer_secret") String consumerSecret
) {}

