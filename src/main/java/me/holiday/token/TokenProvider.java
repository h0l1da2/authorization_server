package me.holiday.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class TokenProvider {

    private final TokenProperties tokenProperties;
    private Key key;

    public TokenProvider(TokenProperties tokenProperties) {
        key = Keys.hmacShaKeyFor(
                tokenProperties
                        .secretkey()
                        .getBytes(StandardCharsets.UTF_8));
        this.tokenProperties = tokenProperties;
    }

    public String createAccessToken(Long memberId) {
        Claims claims = makeAccessClaims(memberId);

        return Jwts.builder()
                .header().add(
                        TokenConstant.ACCESS_TOKEN.name(),
                        TokenConstant.BEARER.name()).and()
                .claims(claims)
                .signWith(key)
                .compact();
    }

    public String createRefreshToken() {
        Claims claims = makeRefreshClaims();

        return Jwts.builder()
                .header().add(
                        TokenConstant.REFRESH_TOKEN.name(),
                        TokenConstant.BEARER.name()).and()
                .claims(claims)
                .signWith(key)
                .compact();
    }

    private Claims makeAccessClaims(Long memberId) {
        Date now = new Date();

        return Jwts.claims()
                .subject(TokenConstant.ACCESS_TOKEN.name())
                .issuedAt(now)
                .expiration(new Date(
                        now.getTime()
                        + tokenProperties.validTime().access()))
                .add(TokenConstant.MEMBER_ID.name(), memberId)
                .build();
    }

    public Claims makeRefreshClaims() {
        Date now = new Date();

        return Jwts.claims()
                .subject(TokenConstant.REFRESH_TOKEN.name())
                .issuedAt(now)
                .expiration(new Date(
                        now.getTime()
                                + tokenProperties.validTime().refresh()))
                .build();
    }
}
