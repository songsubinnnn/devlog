package com.devlog.dto.user;

import com.devlog.domain.user.User;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRequest {
    private String email;
    private String password;
    private String confirmPassword;

    public User toEntity(){
        return User.builder()
            .email(email)
            .password(password)
            .confirmPassword(confirmPassword)
            .build();
    }
}
