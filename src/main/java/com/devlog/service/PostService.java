package com.devlog.service;

import com.devlog.domain.post.Post;
import com.devlog.domain.tag.Tag;
import com.devlog.domain.user.User;
import com.devlog.dto.post.PostRequest;
import com.devlog.dto.post.PostResponse;
import com.devlog.mapper.PostMapper;
import com.devlog.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * The type Post service.
 *
 * @author sbsong
 * @package com.devlog.service
 * @Classname PostService.java
 * @Description "" <PRE> --------------------------------- 개정이력 2025-07-11 sbsong : 최초작성 </PRE>
 * @since 2025 -07-11
 */
@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private static final Logger logger = LoggerFactory.getLogger(PostService.class);
    private final TagService tagService;


    public PostResponse createPost(PostRequest request, User author) {
        Post entity = postMapper.toEntity(request,author);
        Post savedPost = postRepository.save(entity);
        return postMapper.toResponse(savedPost);
    }


    public PostResponse getPost(Long id) {
        Post entity = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("게시글이 존재하지 않습니다."));
        return postMapper.toResponse(entity);
    }


    public Page<PostResponse> getAllPosts(Pageable pageable) {
        Page<Post> entityPage = postRepository.findAllByDeletedAtFalseOrderByCreatedAtDesc(pageable);
        return entityPage.map(postMapper::toResponse);
    }

    @Transactional
    public PostResponse updatePost(Long id, PostRequest request) {
        Post entity = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(("게시글이 존재하지 않습니다.")));

        List<Tag> tagList = tagService.findOrCreateByNames(request.getTags());
        entity.update(request.getTitle(),request.getContent(),request.getThumbnailUrl(),tagList);

        return postMapper.toResponse(entity);
    }


    public void deletePost(Long id) {
        if (!postRepository.existsById(id)) {
            throw new RuntimeException("게시글이 존재하지 않아요.");
        }
        postRepository.deleteById(id);
    }
}
