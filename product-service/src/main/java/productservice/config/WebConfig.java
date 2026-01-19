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

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(roleInterceptor)
                .addPathPatterns("/**");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler("/videos/rooms/**")
                .addResourceLocations("file:videos/rooms/");

        registry.addResourceHandler("/videos/properties/**")
                .addResourceLocations("file:videos/properties/");

        registry.addResourceHandler("/images/roomspackage/**")
                .addResourceLocations("file:images/roomspackage/");
    }
}
