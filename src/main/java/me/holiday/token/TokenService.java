package me.holiday.token;

import lombok.RequiredArgsConstructor;
import me.holiday.auth.domain.Member;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenProvider tokenProvider;
    private final TokenParser tokenParser;

    public boolean isValidToken(String token) {
        return tokenParser.isValid(token);
    }

    public String getAccessToken(Member member) {
        return tokenProvider.createAccessToken(member);
    }

    public Long getMemberId(String token) {
        return tokenParser.getMemberId(token);
    }

    public String getRefreshToken() {
        return tokenProvider.createRefreshToken();
    }

    public String getRoleName(String token) {
        return tokenParser.getRoleName(token);
    }
}
