package com.devlog.controller;

import com.devlog.domain.user.User;
import com.devlog.dto.post.PostRequest;
import com.devlog.dto.post.PostResponse;
import com.devlog.service.PostService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

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

    /**
     * 등록 처리
     *
     * @param request the request
     * @param author  the author
     * @return the response entity
     */
    @PostMapping
    public ResponseEntity<PostResponse> createPost(@ModelAttribute PostRequest request, @RequestParam(required = false) MultipartFile thumbnail, @RequestParam(required = false) List<MultipartFile> attachments, User author, RedirectAttributes ra) {
        author.setId(1L); // test용
        try {
            PostResponse response = postService.createPost(request, thumbnail, attachments, author);
            logger.info("Post created: {}", response.getId());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 수정 처리
     *
     * @param id      the id
     * @param request the request
     * @return the response entity
     */
    @PutMapping("/{id}")
    public ResponseEntity<PostResponse> updatePost(@PathVariable Long id, @RequestParam(required = false) List<Long> existingAttachmentsId, @RequestParam(required = false) MultipartFile thumbnail, @RequestParam(required = false) List<MultipartFile> attachments, @RequestParam List<Long> deletedFilesId, @ModelAttribute PostRequest request) {
        PostResponse response = postService.updatePost(id, existingAttachmentsId, thumbnail, attachments, deletedFilesId, request);
        return ResponseEntity.ok(response);
    }

}
