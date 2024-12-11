package me.holiday.token;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;

@Component
public class TokenParser {

    private final JwtParser jwtParser;

    public TokenParser(TokenProperties tokenProperties) {
        SecretKey key = Keys.hmacShaKeyFor(
                tokenProperties
                        .secretkey()
                        .getBytes(StandardCharsets.UTF_8));

        this.jwtParser = Jwts.parser()
                .verifyWith(key)
                .clock(Date::new)
                .build();
    }

    public boolean isValid(String token) {
        try {
            Map<String, Object> payload = getPayload(token);
            if (!payload.isEmpty()) {
                Long exp = (Long) payload.get(TokenConstant.EXP.getValue());
                Instant instant = Instant.ofEpochSecond(exp);
                LocalDateTime expLocalDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
                return !expLocalDateTime.isBefore(LocalDateTime.now());
            }
            return false;
        } catch (ExpiredJwtException e) {
            return false;
        }
    }

    public Long getMemberId(String token) {
        String memberId = String.valueOf(
                getPayload(token).get(TokenConstant.MEMBER_ID.getValue()));
        return Long.parseLong(memberId);
    }

    private Map<String, Object> getPayload(String token) {
        return (Map<String, Object>) jwtParser.parse(token).getPayload();
    }

    public String getRoleName(String token) {
        return String.valueOf(
                getPayload(token).get(TokenConstant.ROLE.getValue()));
    }
}
