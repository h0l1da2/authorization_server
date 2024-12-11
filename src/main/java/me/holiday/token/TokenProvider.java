package me.holiday.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import me.holiday.auth.domain.Member;
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

    public String createAccessToken(Member member) {
        Claims claims = makeAccessClaims(member);

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

    private Claims makeAccessClaims(Member member) {
        Date now = new Date();

        return Jwts.claims()
                .subject(TokenConstant.ACCESS_TOKEN.name())
                .issuedAt(now)
                .expiration(new Date(
                        now.getTime()
                        + tokenProperties.validTime().access()))
                .add(TokenConstant.MEMBER_ID.name(), member.getId())
                .add(TokenConstant.ROLE.name(), member.getRole())
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
