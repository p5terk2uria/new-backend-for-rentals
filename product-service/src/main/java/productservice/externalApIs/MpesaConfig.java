package productservice.externalApIs;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "mpesa")
@Getter
@Setter
public class MpesaConfig {

    private String businessShortcode;
    private String partyB;
    private String transactionType;
    private String passKey;
    private String callBackUrl;
    private String consumerKey;
    private String consumerSecret;
    private String stkUrl;

}
