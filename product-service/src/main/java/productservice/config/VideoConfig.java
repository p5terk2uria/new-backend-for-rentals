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

    private Property property;
    private Room room;
    private String baseUrl;

    @Getter
    @Setter
    public static class Property {
        private String videos;
    }

    @Getter
    @Setter
    public static class Room {
        private String videos;
        private String images;
    }
}
