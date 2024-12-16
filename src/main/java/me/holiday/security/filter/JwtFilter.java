//package me.holiday.security.filter;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import me.holiday.common.exception.ServerException;
//import me.holiday.security.config.AuthRequestProperties;
//import me.holiday.token.TokenConstant;
//import me.holiday.token.TokenService;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//import java.net.URI;
//import java.net.http.HttpClient;
//import java.net.http.HttpRequest;
//import java.net.http.HttpResponse;
//import java.util.ArrayList;
//
//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class JwtFilter extends OncePerRequestFilter {
//
//    private final TokenService tokenService;
//    private final AuthRequestProperties authReqProperties;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request,
//                                    HttpServletResponse response,
//                                    FilterChain filterChain) throws ServletException, IOException {
//
//        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
//
//        if (authHeader == null
//                || !authHeader.startsWith(TokenConstant.BEARER.getValue())) {
//            log.error("[JWT] Bearer 로 시작 하지 않는 토큰");
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        // "Bearer " 떼기
//        String token = authHeader.substring(TokenConstant.BEARER.getValue().length());
//        boolean isValidToken = tokenService.isValidToken(token);
//        if (!isValidToken) {
//            log.error("[JWT] 토큰 인증 실패");
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        Long memberId = tokenService.getMemberId(token);
//        boolean isTokenExists = isTokenValidFromHttpReqToRedisAuthServer(memberId);
//        if (!isTokenExists) {
//            log.error("[JWT] Redis Server 에 토큰 없음");
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        Authentication auth = getAuthentication(token);
//        SecurityContextHolder.getContext().setAuthentication(auth);
//
//        log.info("[JWT] 권한 인증 성공 - memberId: {}", memberId);
//    }
//
//    private boolean isTokenValidFromHttpReqToRedisAuthServer(Long memberId) {
//        HttpClient httpClient = HttpClient.newHttpClient();
//
//        HttpRequest redisAuthReq = HttpRequest.newBuilder()
//                .uri(URI.create(authReqProperties.uri() + "/access-token?memberId=" + memberId))
//                .headers(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
//                .GET()
//                .build();
//
//        try {
//            HttpResponse<String> redisAuthRes = httpClient
//                    .send(
//                            redisAuthReq,
//                            HttpResponse.BodyHandlers.ofString());
//
//            if (redisAuthRes.statusCode() != 200) {
//                return false;
//            }
//
//            log.info("[JWT] 200 From Redis Auth Server");
//            return true;
//        } catch (InterruptedException | IOException e) {
//            log.error("[JWT] Redis Auth Server Req Error : {}", e.getMessage());
//            throw new ServerException();
//        }
//    }
//
//    private Authentication getAuthentication(String token) {
//        Long adminId = tokenService.getMemberId(token);
//        ArrayList<GrantedAuthority> auths = new ArrayList<>();
//        String roleName = tokenService.getRoleName(token);
//        GrantedAuthority authority = new SimpleGrantedAuthority(roleName);
//        auths.add(authority);
//        UserDetails userDetails = new User(String.valueOf(adminId), "", auths);
//        return new UsernamePasswordAuthenticationToken(userDetails, "", auths);
//    }
//}
