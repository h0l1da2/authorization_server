package me.holiday.token;

public enum TokenConstant {
    ACCESS_TOKEN("accessToken"),
    REFRESH_TOKEN("refreshToken"),
    MEMBER_ID("memberId"),
    BEARER("Bearer ")
    ;

    private String value;

    TokenConstant(String value) {
        this.value = value;
    }
}
