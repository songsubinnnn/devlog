package com.devlog.service;

import com.devlog.domain.file.File;
import com.devlog.domain.file.FileType;
import com.devlog.domain.post.Post;
import com.devlog.domain.tag.Tag;
import com.devlog.domain.user.User;
import com.devlog.dto.file.FileResponse;
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

import java.util.List;
import java.util.stream.Collectors;

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


    public PostResponse getPost(Long id) {
        // 게시글
        Post postEntity = postRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("게시글이 존재하지 않습니다."));
       // 파일
        List<File> files = fileService.getFiles(id);

        // 파일을 thumbnail, attachments로 분류하여 DTO 변환 -> 서비스단
        // 썸네일 (단일)
        File thumbnail = files.stream()
            .filter(f -> f.getFileType() == FileType.THUMBNAIL)
            .findFirst()
            .orElse(null);

        // 첨부파일
        List<File> attachments = files.stream()
            .filter(f -> f.getFileType() == FileType.ATTACHMENT)
            .collect(Collectors.toList());

        return postMapper.toPostDetailResponse(postEntity, thumbnail, attachments);
    }


    public Page<PostResponse> getAllPosts(Pageable pageable) {
        Page<PostWithThumbnailProjection> posts = postRepository.findAllWithThumbnail(pageable);
        return posts.map(proj -> {
            PostResponse dto = new PostResponse();
            dto.setId(proj.getId());
            dto.setTitle(proj.getTitle());
            dto.setCreatedAt(proj.getCreatedAt());
            dto.setAuthorNickname(proj.getAuthorNickname());
            // 썸네일 객체 직접 생성 후 설정
            FileResponse thumbnail = new FileResponse();
            thumbnail.setFilePath(proj.getFilePath());
            dto.setThumbnail(thumbnail);

            fileService.loadThumbnailBase64(dto);
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

}
