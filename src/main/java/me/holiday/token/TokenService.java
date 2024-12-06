package me.holiday.token;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenProvider tokenProvider;
    private final TokenParser tokenParser;

    public boolean tokenValid(String token) {
        return tokenParser.isValid(token);
    }

    public String getAccessToken(Long memberId) {
        return tokenProvider.createAccessToken(memberId);
    }

    public String getRefreshToken() {
        return tokenProvider.createRefreshToken();
    }

}
