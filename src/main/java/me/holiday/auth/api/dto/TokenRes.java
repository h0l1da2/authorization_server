package me.holiday.auth.api.dto;

public record TokenRes() {

    public record MemberIdRes(
            Long memberId
    ) {

    }

    public record AccessTokenRes(
            String accessToken
    ) {

    }

    public record RefreshTokenRes(
            String refreshToken
    ) {

    }

    public record AccessAndRefreshTokenRes(
            String accessToken,
            String refreshToken
    ) {

    }

}
