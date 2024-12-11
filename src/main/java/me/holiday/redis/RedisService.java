package me.holiday.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.holiday.common.exception.ServerException;
import me.holiday.token.TokenConstant;
import me.holiday.token.TokenProperties;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

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
            template.convertAndSend("token", serialize);

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
}
