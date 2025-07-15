package com.devlog.controller;

import com.devlog.domain.user.User;
import com.devlog.dto.post.PostRequest;
import com.devlog.dto.post.PostResponse;
import com.devlog.service.PostService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * The type Post controller.
 *
 * @author sbsong
 * @package com.devlog.controller
 * @Classname PostController.java
 * @Description "" <PRE> --------------------------------- 개정이력 2025-07-11 sbsong : 최초작성 </PRE>
 * @since 2025 -07-11
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostApiController {

    private final PostService postService;
    private static final Logger logger = LoggerFactory.getLogger(PostService.class);


    @PostMapping
    public ResponseEntity<Map<String, String>> createPost(@ModelAttribute PostRequest request, User author) {
        PostResponse response = postService.createPost(request, author);
        logger.info("Post created: {}", response.getId());
        return ResponseEntity.ok(Map.of("redirectUrl", "/posts"));
    }

    /**
     * 게시글 상세 조회
     *
     * @param id the id
     * @return the response entity
     */
    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPost(@PathVariable Long id) {
        PostResponse postResponse = postService.getPost(id);
        return ResponseEntity.ok(postResponse);
    }


    @PutMapping("/{id}")
    public ResponseEntity<PostResponse> updatePost(@PathVariable Long id, @RequestBody PostRequest request) {
        PostResponse updatedPost = postService.updatePost(id, request);
        return ResponseEntity.ok(updatedPost);
    }

    /**
     * 게시글 삭제
     *
     * @param id the id
     * @return the response entity
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }
}
