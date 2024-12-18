package me.holiday.token;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.holiday.auth.api.dto.TokenReq;
import me.holiday.auth.api.dto.TokenRes;
import me.holiday.auth.domain.Member;
import me.holiday.common.exception.AuthException;
import me.holiday.redis.RedisService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenService {

    private final RedisService redisService;
    private final TokenProvider tokenProvider;
    private final TokenParser tokenParser;

    public boolean isValidAccessToken(String token) {
        validAccessTokenByRedis(token);
        return tokenParser.isValid(token);
    }

    public String getAccessToken(Member member) {
        return tokenProvider.createAccessToken(member);
    }

    public Long getMemberId(String token) {
        return tokenParser.getMemberId(token);
    }

    public String getRefreshToken(Long memberId) {
        return tokenProvider.createRefreshToken(memberId);
    }

    public String getRoleName(String token) {
        return tokenParser.getRoleName(token);
    }

    private void validAccessTokenByRedis(String authToken) {
        Long memberId = tokenParser.getMemberId(authToken);

        TokenRes.AccessTokenRes tokenRes = redisService.getToken(memberId);
        if (tokenRes == null
                || !authToken.equals(tokenRes.accessToken())) {
            throw new AuthException(
                    HttpStatus.UNAUTHORIZED,
                    "유효하지 않은 토큰",
                    null);
        }
    }

    public TokenReq saveTokenReq(Long memberId, String accessToken, String refreshToken) {
        return new TokenReq(
                memberId,
                accessToken, tokenProvider.tokenProperties.validTime().access(),
                refreshToken, tokenProvider.tokenProperties.validTime().refresh());
    }

    public void validRefreshToken(final String refreshToken) {
        Long memberId = tokenParser.getMemberId(refreshToken);

        TokenRes.RefreshTokenRes tokenRes = redisService.getRefreshToken(memberId);
        if (tokenRes == null
                || !refreshToken.equals(tokenRes.refreshToken())) {
            throw new AuthException(
                    HttpStatus.UNAUTHORIZED,
                    "유효하지 않은 토큰",
                    null);
        }
    }
}
