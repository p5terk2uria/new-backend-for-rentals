package productservice.externalApIs;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "pesapal")
public class PesaPalConfigurations {

    private String consumerKey;

    private String consumerSecret;

    private String liveAuthenticationUrl;

    private String submitRequestUrl;

    private String currency;

    private String redirectMode;

    private String cancellationUrl;

    private String callbackUrl;

    private String notificationId;

    private String ipnUrl;

    private String ipnRegistrationUrl;

}
