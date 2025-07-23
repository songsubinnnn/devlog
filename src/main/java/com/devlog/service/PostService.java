package com.devlog.service;

import com.devlog.domain.file.File;
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

    private static final Logger logger = LoggerFactory.getLogger(PostService.class);

    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final TagService tagService;
    private final FileService fileService;

    /**
     * Create post post response.
     *
     * @param request the request
     * @param author  the author
     * @return the post response
     */
    @Transactional
    public PostResponse createPost(PostRequest request, User author) {
        Post entity = postMapper.toEntity(request, author);
        File file = fileService.uploadFile(request.getThumbnail()); // 파일 업로드
        entity.setThumbnail(file);
        Post savedPost = postRepository.save(entity);
        logger.info("게시글 생성 완료 ---- ID : {}", savedPost.getId());
        return postMapper.toResponse(savedPost);
    }


    /**
     * Gets post.
     *
     * @param id the id
     * @return the post
     */
    public PostResponse getPost(Long id) {
        Post entity = postRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("게시글이 존재하지 않습니다."));
        entity.increaseViewCount();
        return postMapper.toResponse(entity);
    }


    /**
     * Gets all posts.
     *
     * @param pageable the pageable
     * @return the all posts
     */
    public Page<PostResponse> getAllPosts(Pageable pageable) {
        Page<Post> entityPage = postRepository.findAllByIsDeletedFalseOrderByCreatedAtDesc(pageable);
        return entityPage.map(postMapper::toResponse);
    }

    /**
     * Update post post response.
     *
     * @param id      the id
     * @param request the request
     * @return the post response
     */
    @Transactional
    public PostResponse updatePost(Long id, PostRequest request) {
        Post entity = postRepository.findById(id)
            .orElseThrow(() -> new RuntimeException(("게시글이 존재하지 않습니다.")));

        List<Tag> tagList = tagService.findOrCreateByNames(request.getTags());

        // TODO 파일 업데이트 수정
      //  entity.update(request.getTitle(), request.getContent(), request.getThumbnail(), tagList);

        return postMapper.toResponse(entity);
    }

    /**
     * Soft delete post.
     *
     * @param id the id
     */
    @Transactional
    public void softDeletePost(Long id) {
        Post post = postRepository.findByIdAndIsDeletedFalse(id)
            .orElseThrow(() -> new RuntimeException("게시글이 존재하지 않습니다."));
        post.softDeleted();
    }
}
