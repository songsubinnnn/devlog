package com.devlog.service;

import com.devlog.domain.file.FileType;
import com.devlog.domain.post.Post;
import com.devlog.domain.tag.Tag;
import com.devlog.domain.user.User;
import com.devlog.dto.post.PostRequest;
import com.devlog.dto.post.PostResponse;
import com.devlog.dto.post.PostWithThumbnailProjection;
import com.devlog.mapper.PostMapper;
import com.devlog.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
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
    public PostResponse createPost(PostRequest request, MultipartFile thumbnail, List<MultipartFile> attachments, User author) {
        Post entity = postMapper.toEntity(request, author);
        Post savedPost = postRepository.save(entity);

        // 파일 업로드
        if (thumbnail != null && !thumbnail.isEmpty()) {
            fileService.uploadFile(thumbnail, FileType.THUMBNAIL, savedPost);
        }

        if (attachments != null) {
            List<MultipartFile> resultList = attachments.stream()
                .filter(file -> file != null && !file.isEmpty())
                .toList();
            if (!resultList.isEmpty()) {
                fileService.uploadMultipleFiles(attachments, FileType.ATTACHMENT, savedPost);
            }
        }

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
       // entity.increaseViewCount();
        return postMapper.toResponse(entity);
    }


    /**
     * Gets all posts.
     *
     * @param pageable the pageable
     * @return the all posts
     */
    public Page<PostResponse> getAllPosts(Pageable pageable) {
        Page<PostWithThumbnailProjection> posts = postRepository.findAllWithThumbnail(pageable);
        return posts.map(proj -> {
            PostResponse dto = new PostResponse();
            dto.setId(proj.getId());
            dto.setTitle(proj.getTitle());
            dto.setThumbnailPath(proj.getFilePath());
            loadThumbnailBase64(dto);
            return dto;
        });
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

    private void loadThumbnailBase64(PostResponse post) {
        String path = post.getThumbnailPath();
        if (path != null) {
            try {
                String base64 = convertImageToBase64(path);
                post.setThumbnailBase64(base64);
                post.hasThumbnail();
            } catch (IOException e) {
                logger.warn("썸네일 로드 실패 - 게시글 ID: {}", post.getId());
                post.setThumbnailBase64(null); // fallback
            }
        }
    }

    // 이미지 파일을 Base64로 변환하는 메서드
    private String convertImageToBase64(String imagePath) throws IOException {
        Path path = Paths.get(imagePath);

        if (!Files.exists(path)) {
            return null;
        }

        byte[] imageBytes = Files.readAllBytes(path);
        String base64 = Base64.getEncoder().encodeToString(imageBytes);

        // MIME 타입 추출
        String mimeType = Files.probeContentType(path);
        if (mimeType == null) {
            // 확장자로 MIME 타입 추정
            String fileName = path.getFileName().toString().toLowerCase();
            if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
                mimeType = "image/jpeg";
            } else if (fileName.endsWith(".png")) {
                mimeType = "image/png";
            } else if (fileName.endsWith(".gif")) {
                mimeType = "image/gif";
            } else {
                mimeType = "image/jpeg"; // 기본값
            }
        }

        return "data:" + mimeType + ";base64," + base64;
    }
}
