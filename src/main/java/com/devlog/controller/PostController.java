package com.devlog.controller;


import com.devlog.domain.user.User;
import com.devlog.dto.post.PostRequest;
import com.devlog.dto.post.PostResponse;
import com.devlog.service.PostService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * The type Post view controller.
 *
 * @author sbsong
 * @package com.devlog.controller
 * @Classname PostViewController.java
 * @Description "" <PRE> --------------------------------- 개정이력 2025-07-11 sbsong : 최초작성 </PRE>
 * @since 2025 -07-11
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;

    private static final Logger logger = LoggerFactory.getLogger(PostController.class);


    /**
     * 목록
     *
     * @param model    the model
     * @param pageable the pageable
     * @return the post list
     */
    @GetMapping
    public String getPostList(Model model, @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<PostResponse> postList = postService.getAllPosts(pageable);
        model.addAttribute("postList", postList);
        return "post/list"; // templates/post/list.html
    }

    /**
     * 등록 폼
     *
     * @param model the model
     * @return the string
     */
    @GetMapping("/write")
    public String writePostForm(Model model) {
        model.addAttribute("postDTO", new PostRequest());
        return "post/write";
    }

    /**
     * 등록 처리
     *
     * @param request the request
     * @param author  the author
     * @return the response entity
     */
    @PostMapping
    public String createPost(@ModelAttribute PostRequest request, User author) {
        author.setId(1L); // test용
        PostResponse response = postService.createPost(request, author);
        logger.info("Post created: {}", response.getId());
        return "redirect:/posts";
    }

    /**
     * 수정 폼
     *
     * @param id    the id
     * @param model the model
     * @return the string
     */
    @GetMapping("/edit/{id}")
    public String editPostForm(@PathVariable Long id, Model model) {
        PostResponse response = postService.getPost(id);
        model.addAttribute("postDTO", response);
        return "post/write";
    }

    /**
     * 수정 처리
     *
     * @param id      the id
     * @param request the request
     * @return the response entity
     */
    @PutMapping("/{id}")
    public String updatePost(@PathVariable Long id, @ModelAttribute PostRequest request) {
        PostResponse updatedPost = postService.updatePost(id, request);
        return "redirect:/posts/" + id;
    }

    /**
     * 상세
     *
     * @param id    the id
     * @param model the model
     * @return the post detail
     */
    @GetMapping("/{id}")
    public String getPostDetail(@PathVariable Long id, Model model) {
        PostResponse response = postService.getPost(id);
        model.addAttribute("post", response);
        return "post/detail";
    }

    /**
     * 삭제
     *
     * @param id the id
     * @return the string
     */
    @DeleteMapping("/{id}")
    public String deletePost(@PathVariable Long id) {
        postService.softDeletePost(id);
        return "redirect:/posts"; // 삭제 후 목록 페이지로
    }
}
