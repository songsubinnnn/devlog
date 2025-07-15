package com.devlog.controller;


import com.devlog.dto.PostDTO;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    /**
     * 게시글 작성 화면
     *
     * @param model the model
     * @return the string
     */
    @GetMapping("/write")
    public String writePost(Model model) {
        model.addAttribute("postDTO", new PostDTO());
        return "post/write";
    }
}
