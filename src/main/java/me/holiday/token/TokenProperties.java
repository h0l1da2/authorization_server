package me.holiday.token;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

@ConfigurationProperties(prefix = "app.security.token")
@ConfigurationPropertiesBinding
@ConfigurationPropertiesScan
public record TokenProperties (
        @NestedConfigurationProperty
        ValidTimeProperties validTime,
        String secretkey
) {

}
