package me.holiday.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.holiday.common.exception.AuthException;
import me.holiday.common.exception.ServerException;
import me.holiday.security.config.AuthRequestProperties;
import me.holiday.token.TokenConstant;
import me.holiday.token.TokenProperties;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisService {

    private final AuthRequestProperties authReqProperties;
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

    public String getToken(Long memberId) {
        HttpClient httpClient = HttpClient.newHttpClient();

        HttpRequest redisAuthReq = HttpRequest.newBuilder()
                .uri(URI.create(authReqProperties.uri()
                        + "/access-token?memberId=" + memberId))
                .headers(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .GET()
                .build();

        try {
            HttpResponse<String> redisAuthRes = httpClient
                    .send(
                            redisAuthReq,
                            HttpResponse.BodyHandlers.ofString());

            if (redisAuthRes.statusCode() != 200) {
                throw new AuthException(
                        HttpStatus.UNAUTHORIZED,
                        "토큰 없음",
                        null);
            }

            return redisAuthRes.body();
        } catch (IOException | InterruptedException e) {
            log.error("[Redis] 레디스 서버 통신 오류");
            throw new ServerException();
        }

    }
}
