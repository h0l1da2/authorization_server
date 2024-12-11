package me.holiday.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class CorsConfig {

    private final CorsProperties corsProperties;
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowCredentials(corsProperties.allowed().credentials());
        config.setAllowedOrigins(corsProperties.allowed().origins());
        config.setAllowedHeaders(corsProperties.allowed().headers());
        config.setAllowedMethods(corsProperties.allowed().methods());
        config.setExposedHeaders(List.of(corsProperties.exposedHeaders()));
        config.setMaxAge(corsProperties.maxAge());

        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
