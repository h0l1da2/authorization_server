package me.holiday.auth.service;

import lombok.RequiredArgsConstructor;
import me.holiday.auth.api.dto.SignUpDto;
import me.holiday.auth.domain.Member;
import me.holiday.auth.exception.MemberException;
import me.holiday.auth.repository.MemberRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public void signUp(SignUpDto dto) {
        // 중복 아이디 불가
        boolean isSameUsername = memberRepository.findByUsername(dto.username()).isPresent();
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

//    public void signIn(SignInDto dto) {
//
//    }
}
