package productservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import productservice.roleutils.RoleInterceptor;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final RoleInterceptor roleInterceptor;
    private final VideoConfig videoConfig;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(roleInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/videos/**", "/images/**");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler("/videos/videos/properties/**")
                .addResourceLocations("file:" + videoConfig.getProperty().getVideos() + "/")
                .setCachePeriod(3600);

        registry.addResourceHandler("/videos/videos/rooms/**")
                .addResourceLocations("file:" + videoConfig.getRoom().getVideos() + "/")
                .setCachePeriod(3600);

        registry.addResourceHandler("/videos/images/rooms/**")
                .addResourceLocations("file:" + videoConfig.getRoom().getImages() + "/")
                .setCachePeriod(3600);
    }
}