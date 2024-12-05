package me.holiday.auth.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

@ConfigurationProperties(prefix = "app.security.cors")
@ConfigurationPropertiesBinding
@ConfigurationPropertiesScan
public record CorsProperties(
        @NestedConfigurationProperty
        CorsAllowedProperties allowed,
        String exposedHeaders,
        Long maxAge
) {
        public CorsProperties {
                if (exposedHeaders == null) {
                        exposedHeaders = "*";
                }
                if (maxAge == null) {
                        maxAge = 1800L;
                }
        }
}
