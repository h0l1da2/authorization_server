package me.holiday.auth.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.holiday.auth.api.dto.SignUpDto;
import me.holiday.auth.service.AuthService;
import org.springframework.web.bind.annotation.*;

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
}
