package com.devlog.service;

import com.devlog.domain.file.File;
import com.devlog.domain.file.FileType;
import com.devlog.domain.post.Post;
import com.devlog.dto.post.PostResponse;
import com.devlog.repository.FileRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * @author sbsong
 * @package com.devlog.service
 * @Classname FileService.java
 * @Description ""
 * <PRE>
 * ---------------------------------
 * 개정이력
 * 2025-07-21 sbsong : 최초작성
 * </PRE>
 * @since 2025-07-21
 */
@Service
@RequiredArgsConstructor
@Transactional
public class FileService {
    private static final Logger log = LoggerFactory.getLogger(FileService.class);
    private final FileRepository fileRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @PostConstruct // 의존성 주입 완료 후
    public void initUploadDirectory() {
        try {
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
                log.info("업로드 디렉토리 생성:{}", uploadPath.toAbsolutePath());
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("업로드 디렉토리 생성 실패: " + uploadDir, e);
        }
    }

    public List<File> getFiles(Long id) { // 서비스에서 엔티티 -> dto 변환
        return fileRepository.findByPostIdAndIsDeletedFalse(id);

    }

    public void markAsDeleted(String deletedFilesId) {
        // 삭제된 파일 처리
        String[] deletedFilesIdArr = deletedFilesId.split(",");
        for(String fileId : deletedFilesIdArr) {
                fileRepository.findById(Long.valueOf(fileId)).ifPresent(file -> {
                    file.setIsDeleted(true);
                    file.setDeletedAt(LocalDateTime.now());
                    fileRepository.save(file);
                });
        }
    }

    public File uploadFile(MultipartFile file, FileType fileType, Post post) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("업로드 할 파일이 없습니다.");
        }

        String originFileName = file.getOriginalFilename();
        if (originFileName == null || originFileName.trim().isEmpty()) {
            throw new IllegalArgumentException("파일명이 존재하지 않습니다.");
        }
        String ext = getExtension(originFileName);
        String storedFileName = UUID.randomUUID() + "." + ext; // 저장 파일명은 랜덤값으로 조합
        String fullPath = Paths.get(uploadDir, storedFileName).toString();

        try {
            file.transferTo(new java.io.File(fullPath));
        } catch (IOException e) {
            throw new IllegalArgumentException("파일 저장 실패", e);
        }

        File entity = File.builder()
            .originalFileName(originFileName)
            .storedFileName(storedFileName)
            .filePath(fullPath)
            .fileUrl("/uploads/" + storedFileName)
            .size(file.getSize())
            .fileType(fileType)
            .post(post)
            .build();
        return fileRepository.save(entity);

    }

    public List<File> uploadMultipleFiles(List<MultipartFile> files, FileType fileType, Post post) {
        if (files == null || files.isEmpty()) {
            return Collections.emptyList();
        }
        return files.stream()
            .map(file -> uploadFile(file, fileType, post))
            .toList();
    }

    private String getExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            throw new IllegalArgumentException("확장자가 없는 파일입니다.");
        }
        return fileName.substring(fileName.lastIndexOf('.') + 1);
    }


    public void loadThumbnailBase64(PostResponse post) {
        String path = post.getThumbnail().getFilePath();
        if (path != null) {
            try {
                String base64 = convertImageToBase64(path);
                post.getThumbnail().setBase64(base64);
                post.hasThumbnail();
            } catch (IOException e) {
                log.warn("썸네일 로드 실패 - 게시글 ID: {}", post.getId());
                post.getThumbnail().setBase64(null);
            }
        }
    }

    // 이미지 파일을 Base64로 변환하는 메서드
    public String convertImageToBase64(String imagePath) throws IOException {
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
