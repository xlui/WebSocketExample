package app.xlui.example.im.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/broadcast").setViewName("/broadcast");
        registry.addViewController("/group").setViewName("/group");
        registry.addViewController("/chat").setViewName("/chat");
    }
}
