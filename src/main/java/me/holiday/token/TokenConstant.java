package me.holiday.token;

import lombok.Getter;

@Getter
public enum TokenConstant {
    ACCESS("access"),
    REFRESH("refresh"),
    ACCESS_TOKEN("accessToken"),
    REFRESH_TOKEN("refreshToken"),
    MEMBER_ID("memberId"),
    BEARER("Bearer "),
    EXP("exp"),
    ROLE("role"),
    TOKEN("token"),
    ACCESS_TOKEN_KEY_NAME("_access"),
    REFRESH_TOKEN_KEY_NAME("_refresh")
    ;

    private String value;

    TokenConstant(String value) {
        this.value = value;
    }
}
