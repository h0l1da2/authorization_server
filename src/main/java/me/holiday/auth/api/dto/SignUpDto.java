package me.holiday.auth.api.dto;

import jakarta.validation.constraints.NotBlank;
import me.holiday.auth.domain.Member;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;

public record SignUpDto(
        @NotBlank
        String username,
        @NotBlank
        String password,
        @NotBlank
        String name,
        @NotBlank
        String phoneNumber
) {
        public Member toEntity(BCryptPasswordEncoder encoder) {
                return new Member(
                        null,
                        username,
                        encoder.encode(password),
                        name,
                        phoneNumber,
                        LocalDateTime.now()
                );
        }

}