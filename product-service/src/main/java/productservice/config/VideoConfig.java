package productservice.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "videos")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class VideoConfig {

    private String  folder;

    private String baseUrl;
}
