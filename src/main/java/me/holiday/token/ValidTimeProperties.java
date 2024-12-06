package me.holiday.token;

import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;

@ConfigurationPropertiesBinding
public record ValidTimeProperties(
        Long access,
        Long refresh
) {
}
