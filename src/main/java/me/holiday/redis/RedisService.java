package me.holiday.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.holiday.auth.api.dto.TokenReq;
import me.holiday.auth.api.dto.TokenRes;
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
    private final ObjectMapper mapper;

    public void sendLoginTokenMessage(TokenReq tokenReq) {
        log.info("[TokenReq] {}", tokenReq.toString());
        try {
            String tokenReqStrBody = mapper.writeValueAsString(tokenReq);
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(authReqProperties.uri() + "/token/save"))
                    .POST(HttpRequest.BodyPublishers.ofString(tokenReqStrBody))
                    .headers(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .build();

            HttpResponse<String> response = httpClient
                    .send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                log.error("[Auth] 인증 서버 Not 200");
                throw new ServerException();
            }
            if (response.statusCode() == 403) {
                log.error("[Auth] 토큰 저장 X");
            }

        } catch (JsonProcessingException e) {
            log.error("[Redis] Json 파싱 에러");
            throw new ServerException();
        } catch (IOException | InterruptedException e) {
            log.error("[Auth] 인증 서버 요청 에러");
            throw new ServerException();
        }

    }

    public TokenRes.AccessTokenRes getToken(Long memberId) {
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

            return mapper.readValue(redisAuthRes.body(), TokenRes.AccessTokenRes.class);
        } catch (IOException | InterruptedException e) {
            log.error("[Redis] 레디스 서버 통신 오류");
            throw new ServerException();
        }

    }
}
