package me.holiday.token;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.holiday.auth.domain.Member;
import me.holiday.common.exception.AuthException;
import me.holiday.redis.RedisService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenService {

    private final RedisService redisService;
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

    public void validTokenByRedis(String authToken) {
        Long memberId = tokenParser.getMemberId(authToken);

        String tokenByRedis = redisService.getToken(memberId);
        log.info("{}", tokenByRedis);
        log.info("{}", authToken);
        if (!StringUtils.hasText(tokenByRedis)
                || !tokenByRedis.equals(authToken)) {
            throw new AuthException(
                    HttpStatus.UNAUTHORIZED,
                    "유효하지 않은 토큰",
                    null);
        }
    }
}
