package me.holiday.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.holiday.auth.domain.Member;
import me.holiday.auth.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Optional<Member> findByUsername(String username) {
        return memberRepository.findByUsername(username);
    }

    public void save(Member member) {
        memberRepository.save(member);
    }

    public Optional<Member> findById(final Long memberId) {
        return memberRepository.findById(memberId);
    }
}
