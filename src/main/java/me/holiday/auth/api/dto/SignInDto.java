package me.holiday.auth.api.dto;

import jakarta.validation.constraints.NotBlank;

public record SignInDto(

) {
        public record SignInReqDto(
                @NotBlank
                String username,
                @NotBlank
                String password
        ) {

        }

        public record SignInResDto(
                String accessToken,
                String refreshToken
        ) {

        }
}
