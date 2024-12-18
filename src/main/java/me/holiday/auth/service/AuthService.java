package me.holiday.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.holiday.auth.api.dto.SignInDto.SignInRes;
import me.holiday.auth.api.dto.SignUpDto;
import me.holiday.auth.api.dto.TokenReq;
import me.holiday.auth.api.dto.TokenRes.MemberIdRes;
import me.holiday.auth.domain.Member;
import me.holiday.auth.exception.MemberException;
import me.holiday.common.annotation.log.LogExecution;
import me.holiday.common.exception.AuthException;
import me.holiday.redis.RedisService;
import me.holiday.token.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

import static me.holiday.auth.api.dto.SignInDto.SignInReq;
import static me.holiday.auth.api.dto.TokenRes.AccessAndRefreshTokenRes;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberService memberService;
    private final TokenService tokenService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RedisService redisService;

    @LogExecution(message = "회원 가입 요청")
    public void signUp(SignUpDto dto) {
        // 중복 아이디 불가
        boolean isSameUsername = memberService.findByUsername(dto.username()).isPresent();
        if (isSameUsername) {
            throw new MemberException(
                    HttpStatus.BAD_REQUEST,
                    "유저 아이디 중복",
                    Map.of("username", dto.username())
            );
        }
        Member member = dto.toEntity(passwordEncoder);
        memberService.save(member);
    }

    @LogExecution(message = "로그인 요청")
    public SignInRes signIn(SignInReq dto) {
        Member member = memberService.findByUsername(dto.username())
                .orElseThrow(() -> new MemberException(
                        HttpStatus.NOT_FOUND,
                        "로그인 실패",
                        Map.of("username", dto.username())
                ));

        // 비밀 번호 검증
        member.validPwd(dto.password(), passwordEncoder);

        String accessToken = tokenService.getAccessToken(member);
        String refreshToken = tokenService.getRefreshToken(member.getId());

        TokenReq tokenReq = tokenService.saveTokenReq(member.getId(), accessToken, refreshToken);
        // Redis 저장
        redisService.sendLoginTokenMessage(tokenReq);
        return new SignInRes(accessToken, refreshToken);
    }

    @LogExecution(message = "토큰 검증 성공")
    public MemberIdRes validAccessToken(String accessToken) {
        boolean isValid = tokenService.isValidAccessToken(accessToken);

        if (!isValid) {
            throw new AuthException(
                    HttpStatus.UNAUTHORIZED,
                    "토큰 검증 실패",
                    null);
        }

        return new MemberIdRes(
                tokenService.getMemberId(accessToken));
    }

    @LogExecution(message = "리프레쉬 토큰 검증 후 액세스/리프레쉬 토큰 반환")
    public AccessAndRefreshTokenRes validRefreshToken(final String refreshToken) {
        tokenService.validRefreshToken(refreshToken);

        Long memberId = tokenService.getMemberId(refreshToken);

        // 토큰이 유효하더라도 memberId 가 맞는지 다시 확인할 필요가 있음.
        Member member = memberService.findById(memberId)
                .orElseThrow(() -> new MemberException(HttpStatus.NOT_FOUND,
                        "토큰 아이디로 멤버 없음",
                        null));

        String newAccessToken = tokenService.getAccessToken(member);
        String newRefreshToken = tokenService.getRefreshToken(member.getId());

        return new AccessAndRefreshTokenRes(newAccessToken, newRefreshToken);
    }
}
