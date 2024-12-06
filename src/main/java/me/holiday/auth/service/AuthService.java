package me.holiday.auth.service;

import lombok.RequiredArgsConstructor;
import me.holiday.auth.api.dto.SignUpDto;
import me.holiday.auth.domain.Member;
import me.holiday.auth.exception.MemberException;
import me.holiday.auth.repository.MemberRepository;
import me.holiday.common.annotation.log.LogExecution;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

import static me.holiday.auth.api.dto.SignInDto.SignInReqDto;
import static me.holiday.auth.api.dto.SignInDto.SignInResDto;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @LogExecution("회원 가입 요청")
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

    @LogExecution("로그인 요청")
    public SignInResDto signIn(SignInReqDto dto) {
        Member member = findByUsername(dto.username())
                .orElseThrow(() -> new MemberException(
                        HttpStatus.NOT_FOUND,
                        "로그인 실패",
                        Map.of("username", dto.username())
                ));

        // 비밀 번호 검증
        member.validPwd(dto.password(), passwordEncoder);

        return new SignInResDto("accessToken", "refreshToken");
    }

    private Optional<Member> findByUsername(String username) {
        return memberRepository.findByUsername(username);
    }

//    public void signIn(SignInDto dto) {
//
//    }
}
