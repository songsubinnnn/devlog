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

        // 태그
        List<String> tagNames = postEntity.getPostTags().stream()
            .map(postTag -> postTag.getTag().getName())
            .collect(Collectors.toList());

        // 파일을 thumbnail, attachments로 분류하여 DTO 변환 -> 서비스단
        List<File> files = fileService.getFiles(id);

        // 썸네일 (단일)
        File thumbnail = files.stream()
            .filter(f -> f.getFileType() == FileType.THUMBNAIL)
            .findFirst()
            .orElse(null);

        // 첨부파일
        List<File> attachments = files.stream()
            .filter(f -> f.getFileType() == FileType.ATTACHMENT)
            .toList();

        return postMapper.toPostDetailResponse(postEntity, thumbnail, attachments,tagNames);
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
            if (proj.getFilePath() != null && !proj.getFilePath().isEmpty()) {
                FileResponse thumbnail = new FileResponse();
                thumbnail.setFilePath(proj.getFilePath());
                dto.setThumbnail(thumbnail);
                fileService.loadThumbnailBase64(dto);
            }

            return dto;
        });
    }


    @Transactional
    public PostResponse updatePost(Long id, MultipartFile thumbnail, List<MultipartFile> attachments, String deletedFilesId, PostRequest request) {
        Post post = postRepository.findById(id)
            .orElseThrow(() -> new RuntimeException(("게시글이 존재하지 않습니다.")));

        List<Tag> tagList = tagService.findOrCreateByNames(request.getTags());

        post.update(request.getTitle(), request.getContent(), tagList);

        // 파일 삭제 처리
        if (deletedFilesId != null && !deletedFilesId.isEmpty()) {
            fileService.markAsDeleted(deletedFilesId);
        }

        // 파일 업로드
        if (thumbnail != null && !thumbnail.isEmpty()) {
            fileService.uploadFile(thumbnail, FileType.THUMBNAIL, post);
        }

        if (attachments != null) {
            List<MultipartFile> resultList = attachments.stream()
                .filter(file -> file != null && !file.isEmpty())
                .toList();
            if (!resultList.isEmpty()) {
                fileService.uploadMultipleFiles(attachments, FileType.ATTACHMENT, post);
            }
        }

        return postMapper.toResponse(post);
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
