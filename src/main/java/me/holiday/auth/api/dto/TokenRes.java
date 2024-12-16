package me.holiday.auth.api.dto;

public record TokenRes() {

    public record AccessTokenRes(
            String accessToken
    ) {

    }

}
