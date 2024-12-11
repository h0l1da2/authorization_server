package me.holiday.security.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationProperties(prefix = "app.security.auth.server")
@ConfigurationPropertiesBinding
@ConfigurationPropertiesScan
public record AuthRequestProperties(
        String uri
) {
}
