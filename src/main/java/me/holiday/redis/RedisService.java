package me.holiday.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.holiday.common.exception.ServerException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisSerializable redisSerializable;
    private final RedisTemplate template;

    public void sendLoginTokenMessage(Map<Object, Object> message) {
        try {
            String serialize = redisSerializable.serialize(message);
            template.convertAndSend("token", serialize);
        } catch (JsonProcessingException e) {
            log.error("[Redis] JSON Parse Error : {}", e.getMessage());
            throw new ServerException();
        }
    }
}
