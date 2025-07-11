package com.devlog.controller;

import com.devlog.dto.PostDTO;
import com.devlog.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    /**
     * 게시글 생성
     *
     * @param postDTO the post dto
     * @return the response entity
     */
    @PostMapping
    public ResponseEntity<Long> createPost(@RequestBody PostDTO postDTO){
        Long id = postService.createPost(postDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

    /**
     * 게시글 상세 조회
     *
     * @param id the id
     * @return the response entity
     */
    @GetMapping("/{id}")
    public ResponseEntity<PostDTO> getPost(@PathVariable Long id){
        PostDTO postDTO = postService.getPost(id);
        return ResponseEntity.ok(postDTO);
    }


    /**
     * 게시글 수정
     *
     * @param id      the id
     * @param postDTO the post dto
     * @return the response entity
     */
    @PutMapping("/{id}")
    public ResponseEntity<PostDTO> updatePost(@PathVariable Long id, @RequestBody PostDTO postDTO){
        PostDTO updatedPost = postService.updatePost(id, postDTO);
        return ResponseEntity.ok(updatedPost);
    }

    /**
     * 게시글 삭제
     *
     * @param id the id
     * @return the response entity
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id){
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }
}
