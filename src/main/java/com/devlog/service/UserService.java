package com.devlog.service;

import com.devlog.domain.user.User;
import com.devlog.dto.user.UserJoinRequest;
import com.devlog.dto.user.UserResponse;
import com.devlog.mapper.UserMapper;
import com.devlog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class UserService {
    public final UserRepository userRepository;
    public final UserMapper userMapper;
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Transactional
    public UserResponse join(UserJoinRequest userJoinRequest) {
        User userEntity = userMapper.toEntity(userJoinRequest);
        User saved = userRepository.save(userEntity);

        log.info("유저 등록 완료 ---- ID : {}", saved.getId());
        return userMapper.toResponse(saved);
    }
    //TODO join, login

}
