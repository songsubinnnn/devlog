package com.devlog.service;

import com.devlog.dto.PostDTO;
import com.devlog.entity.PostEntity;
import com.devlog.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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
    private static final Logger logger = LoggerFactory.getLogger(PostService.class);

    /**
     * 게시글 작성
     *
     * @param postDTO the post dto
     * @return the long
     */
    public Long createPost(PostDTO postDTO) {
        PostEntity entity = postRepository.save(postDTO.toEntity());
        logger.info(entity.toString());
        return entity.getId();
    }

    /**
     * 게시글 상세 조회
     *
     * @param id the id
     * @return the post dto
     */
    public PostDTO getPost(Long id) {
        PostEntity entity = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("게시글이 존재하지 않습니다."));
        return PostDTO.fromEntity(entity);
    }

    /**
     * 게시글 목록 조회
     *
     * @return the list
     */
    public Page<PostDTO> getAllPosts(int page, int size) {
        Pageable pageable = PageRequest.of(page,size, Sort.by("createdAt").descending());
        Page<PostEntity> entityPage = postRepository.findAllByOrderByCreatedAtDesc(pageable);

        return entityPage.map(PostDTO::fromEntity);
    }

    /**
     * 게시글 수정
     *
     * @param id      the id
     * @param postDTO the post dto
     * @return the post dto
     */
    public PostDTO updatePost(Long id, PostDTO postDTO) {
        PostEntity entity = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(("게시글이 존재하지 않습니다.")));

        entity.setTitle(postDTO.getTitle());
        entity.setContent(postDTO.getContent());
        PostEntity updatedEntity = postRepository.save(entity);
        return PostDTO.fromEntity(updatedEntity);
    }

    /**
     * 게시글 삭제
     *
     * @param id the id
     */
    public void deletePost(Long id) {
        if (!postRepository.existsById(id)) {
            throw new RuntimeException("게시글이 존재하지 않아요.");
        }
        postRepository.deleteById(id);
    }
}
