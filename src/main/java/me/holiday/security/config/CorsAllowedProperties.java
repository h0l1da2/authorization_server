package me.holiday.security.config;

import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;

import java.util.ArrayList;
import java.util.List;

@ConfigurationPropertiesBinding
public record CorsAllowedProperties(
        List<String> headers,
        List<String> methods,
        List<String> origins,
        Boolean credentials
) {
    public CorsAllowedProperties {
        if (headers == null || headers.isEmpty()) {
            headers = new ArrayList<>();
            headers.add("*");
        }
        if (methods == null || methods.isEmpty()) {
            methods = new ArrayList<>();
            methods.add("*");
        }
        if (origins == null) {
            origins = new ArrayList<>();
            origins.add("*");
        }
        if (credentials == null) {
            credentials = false;
        }
    }
}
