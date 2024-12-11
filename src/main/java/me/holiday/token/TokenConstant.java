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
    ROLE("role")
    ;

    private String value;

    TokenConstant(String value) {
        this.value = value;
    }
}
