package com.devlog.dto.user;

import com.devlog.domain.user.User;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    private String username;
    private String email;
    private String nickname;

    public static UserResponse toResponse(User user){
        return new UserResponse(user.getUsername(), user.getEmail(), user.getNickname());
    }
}
