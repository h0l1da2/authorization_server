package me.holiday.token;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenProvider tokenProvider;

    public String getAccessToken(Long memberId) {
        return tokenProvider.createAccessToken(memberId);
    }

    public String getRefreshToken() {
        return tokenProvider.createRefreshToken();
    }

}
