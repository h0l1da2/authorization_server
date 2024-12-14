package me.holiday.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.holiday.auth.api.dto.SignInDto.SignInRes;
import me.holiday.auth.api.dto.SignUpDto;
import me.holiday.auth.domain.Member;
import me.holiday.auth.exception.MemberException;
import me.holiday.auth.repository.MemberRepository;
import me.holiday.common.annotation.log.LogExecution;
import me.holiday.common.exception.AuthException;
import me.holiday.redis.RedisService;
import me.holiday.token.TokenConstant;
import me.holiday.token.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

import static me.holiday.auth.api.dto.SignInDto.SignInReq;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final TokenService tokenService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RedisService redisService;

    @LogExecution(message = "회원 가입 요청")
    public void signUp(SignUpDto dto) {
        // 중복 아이디 불가
        boolean isSameUsername = findByUsername(dto.username()).isPresent();
        if (isSameUsername) {
            throw new MemberException(
                    HttpStatus.BAD_REQUEST,
                    "유저 아이디 중복",
                    Map.of("username", dto.username())
            );
        }
        Member member = dto.toEntity(passwordEncoder);
        memberRepository.save(member);
    }

    @LogExecution(message = "로그인 요청")
    public SignInRes signIn(SignInReq dto) {
        Member member = findByUsername(dto.username())
                .orElseThrow(() -> new MemberException(
                        HttpStatus.NOT_FOUND,
                        "로그인 실패",
                        Map.of("username", dto.username())
                ));

        // 비밀 번호 검증
        member.validPwd(dto.password(), passwordEncoder);

        String accessToken = tokenService.getAccessToken(member);
        String refreshToken = tokenService.getRefreshToken();

        // Redis 저장
        redisService.sendLoginTokenMessage(
                Map.of(
                        member.getId() + TokenConstant.ACCESS_TOKEN_KEY_NAME.getValue(), accessToken,
                        member.getId() + TokenConstant.REFRESH_TOKEN_KEY_NAME.getValue(), refreshToken
        ));
        return new SignInRes(accessToken, refreshToken);
    }

    private Optional<Member> findByUsername(String username) {
        return memberRepository.findByUsername(username);
    }

    @LogExecution(message = "토큰 검증 성공")
    public void validToken(String authToken) {
        if (!authToken.startsWith(TokenConstant.BEARER.getValue())) {
            throw new AuthException(
                    HttpStatus.BAD_REQUEST,
                    "토큰 재확인",
                    Map.of(TokenConstant.TOKEN.getValue(), authToken));

        }
        // 토큰 앞에 붙은 "Bearer " 제거
        authToken = authToken.substring(TokenConstant.BEARER.getValue().length());

        tokenService.validTokenByRedis(authToken);
    }

}
