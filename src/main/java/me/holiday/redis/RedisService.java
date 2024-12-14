package me.holiday.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.holiday.common.exception.AuthException;
import me.holiday.common.exception.ServerException;
import me.holiday.token.TokenConstant;
import me.holiday.token.TokenProperties;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisSerializable redisSerializable;
    private final StringRedisTemplate template;
    private final TokenProperties tokenProperties;

    public void sendLoginTokenMessage(Map<String, String> message) {
        try {
            String serialize = redisSerializable.serialize(message);
            template.convertAndSend(TokenConstant.TOKEN.getValue(), serialize);

            message.keySet().forEach(key -> {
                if (key.contains(TokenConstant.ACCESS.getValue())) {
                    template.opsForValue().set(
                            key,
                            message.get(key),
                            tokenProperties.validTime().access());
                }

                if (key.contains(TokenConstant.REFRESH.getValue())) {
                    template.opsForValue().set(
                            key,
                            message.get(key),
                            tokenProperties.validTime().refresh());
                }
            });

            log.info("[Redis] 메시지 밣행 : {}",  message);
        } catch (JsonProcessingException e) {
            log.error("[Redis] JSON Parse Error : {}", e.getMessage());
            throw new ServerException();
        }
    }

    public void isValidToken(String authToken, Long memberId) {
        String token = template.opsForValue().get(memberId + TokenConstant.ACCESS_TOKEN_KEY_NAME.getValue());
        if (StringUtils.hasText(token) || !token.equals(authToken)) {
            throw new AuthException(
                    HttpStatus.UNAUTHORIZED,
                    "유효하지 않은 토큰",
                    null);
        }
    }
}
