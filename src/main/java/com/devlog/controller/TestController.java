package com.devlog.controller;

import com.devlog.entity.PostEntity;
import com.devlog.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author sbsong
 * @package com.devlog.controller
 * @Classname TestController.java
 * @Description ""
 * <PRE>
 * ---------------------------------
 * 개정이력
 * 2025-07-11 sbsong : 최초작성
 * </PRE>
 * @since 2025-07-11
 */
@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {

    private final PostRepository postRepository;

    @GetMapping("/create")
    public String createPost(){
        PostEntity post = PostEntity.builder()
                .title("first post")
                .content("포스트 1")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        postRepository.save(post);
        return "success";
    }

    @GetMapping("/list")
    public List<PostEntity> list(){
        return postRepository.findAll();
    }
}
