package com.devlog.controller;


import com.devlog.dto.post.PostRequest;
import com.devlog.dto.post.PostResponse;
import com.devlog.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

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
public class PostViewController {
    private final PostService postService;


    @GetMapping
    public String getPostList(Model model, @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<PostResponse> postList = postService.getAllPosts(pageable);
        model.addAttribute("postList", postList);
        return "post/list"; // templates/post/list.html
    }

    @GetMapping("/write")
    public String writePost(Model model) {
        model.addAttribute("postDTO", new PostRequest());
        return "post/write";
    }

    @GetMapping("/edit/{id}")
    public String editPost(@PathVariable Long id, Model model) {
        PostResponse response = postService.getPost(id);
        model.addAttribute("postDTO", response);
        return "post/write";
    }

    @GetMapping("/{id}")
    public String getPostDetail(@PathVariable Long id, Model model) {
        PostResponse response = postService.getPost(id);
        model.addAttribute("post", response);
        return "post/detail";
    }
}
