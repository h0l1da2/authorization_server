package me.holiday.auth.api.dto;

import jakarta.validation.constraints.NotBlank;

public record SignInDto(

) {
        public record SignInReq(
                @NotBlank
                String username,
                @NotBlank
                String password
        ) {

        }

        public record SignInRes(
                String accessToken,
                String refreshToken
        ) {

        }
}
