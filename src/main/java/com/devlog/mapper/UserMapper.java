package com.devlog.mapper;

import com.devlog.domain.user.User;
import com.devlog.dto.user.UserJoinRequest;
import com.devlog.dto.user.UserResponse;
import org.springframework.stereotype.Component;


@Component
public class UserMapper {

    public User toEntity(UserJoinRequest userJoinRequest){
        return User.builder()
            .email(userJoinRequest.getEmail())
            .password(userJoinRequest.getPassword())
            .role(User.Role.USER) // test용
            .nickname(userJoinRequest.getNickname())
            .build();
    }

    public UserResponse toResponse(User user){
        return UserResponse.builder()
            .email(user.getEmail())
            .username(user.getUsername())
            .nickname(user.getNickname())
            .build();
    }
}
