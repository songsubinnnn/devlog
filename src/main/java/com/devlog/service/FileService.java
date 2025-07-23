package com.devlog.service;

import com.devlog.domain.file.File;
import com.devlog.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Paths;
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

    private final FileRepository fileRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    public File uploadFile(MultipartFile multipartFile) {
        String originFileName = multipartFile.getOriginalFilename();
        String ext = getExtension(originFileName);
        String storedFileName = UUID.randomUUID() + "." + ext; // 저장 파일명은 랜덤값으로 조합
        String fullPath = Paths.get(uploadDir, storedFileName).toString();

        try {
            multipartFile.transferTo(new java.io.File(fullPath));
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 실패", e);
        }
        File entity = File.builder()
            .originalFileName(originFileName)
            .storedFileName(storedFileName)
            .filePath(fullPath)
            .fileUrl("/uploads/" + storedFileName)
            .size(multipartFile.getSize())
            .build();

        File saved = fileRepository.save(entity);
        return saved;
    }

    private String getExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        return dotIndex == -1 ? "" : fileName.substring(dotIndex + 1);
    }

}
