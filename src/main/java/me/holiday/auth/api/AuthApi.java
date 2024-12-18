package me.holiday.auth.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.holiday.auth.api.dto.SignInDto;
import me.holiday.auth.api.dto.SignInDto.SignInRes;
import me.holiday.auth.api.dto.SignUpDto;
import me.holiday.auth.api.dto.TokenRes;
import me.holiday.auth.service.AuthService;
import org.springframework.web.bind.annotation.*;

import static me.holiday.auth.api.dto.TokenRes.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AuthApi {

    private final AuthService authService;

    /**
     * 회원 가입
     * @param dto : 아이디, 비번, 이름, 휴대폰 번호
     */
    @PostMapping("/sign-up")
    public void signUp(@RequestBody @Valid SignUpDto dto) {
        authService.signUp(dto);
    }

    /**
     * 로그인
     * @param dto : 아이디, 비번
     * @return : access, refresh Token
     */
    @PostMapping("/sign-in")
    public SignInRes signIn(@RequestBody @Valid SignInDto.SignInReq dto) {
        return authService.signIn(dto);
    }

    /**
     * 액세스 토큰 검증
     * - 유효하지 않을 경우 예외 발생
     * @param token : 액세스 토큰
     */
    @GetMapping("/validation/access-token")
    public void validAccessToken(@RequestParam String token) {
        authService.validToken(token);
    }

    /**
     * 리프레쉬 토큰 검증
     * @param refreshToken : 리프레쉬 토큰
     * @return : 리프레쉬 / 액세스 토큰
     */
    @GetMapping("/validation/refresh-token")
    public AccessAndRefreshTokenRes validRefreshToken(@RequestParam String refreshToken) {
        return authService.validRefreshToken(refreshToken);
    }
}
