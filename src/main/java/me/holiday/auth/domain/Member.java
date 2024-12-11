package me.holiday.auth.domain;

import jakarta.persistence.*;
import lombok.*;
import me.holiday.auth.exception.MemberException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;

    @Enumerated(EnumType.STRING)
    private RoleName role;
    private String name;
    private String phoneNumber;

    private LocalDateTime createdAt;

    public void validPwd(String password, BCryptPasswordEncoder encoder) {
        boolean matches = encoder.matches(password, this.password);
        if (!matches) {
            throw new MemberException(
                    HttpStatus.NOT_FOUND,
                    "로그인 실패",
                    Map.of("username", this.username)
            );
        }
    }

}
