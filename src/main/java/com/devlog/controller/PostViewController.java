package com.devlog.controller;


import com.devlog.dto.PostDTO;
import com.devlog.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author sbsong
 * @package com.devlog.controller
 * @Classname PostViewController.java
 * @Description ""
 * <PRE>
 * ---------------------------------
 * 개정이력
 * 2025-07-11 sbsong : 최초작성
 * </PRE>
 * @since 2025-07-11
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostViewController {
    private final PostService postService;

    @GetMapping
    public String getPosts(Model model) {
        List<PostDTO> postList =  postService.getAllPosts();
        model.addAttribute("postList", postList);
        return "post/list"; // templates/post/list.html
    }

    @GetMapping("/write")
    public String writePost(Model model) {
        model.addAttribute("postDto", new PostDTO());
        return "post/write";
    }
}
