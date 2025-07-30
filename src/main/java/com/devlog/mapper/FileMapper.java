package com.devlog.mapper;

import com.devlog.domain.file.File;
import com.devlog.dto.file.FileResponse;
import org.springframework.stereotype.Component;

/**
 * @author sbsong
 * @package com.devlog.mapper
 * @Classname FileMapper.java
 * @Description ""
 * <PRE>
 * ---------------------------------
 * 개정이력
 * 2025-07-23 sbsong : 최초작성
 * </PRE>
 * @since 2025-07-23
 */
@Component
public class FileMapper {
    public File toEntity(String originFileNm, String storedFileNm, String filePath, String fileUrl, Long fileSize) {
        return File.builder()
            .originalFileName(originFileNm)
            .storedFileName(storedFileNm)
            .filePath(filePath)
            .fileUrl(fileUrl)
            .size(fileSize)
            .build();
    }

    public FileResponse toResponse(File fileEntity) {
        return FileResponse.builder()
            .originalFileName(fileEntity.getOriginalFileName())
            .storedFileName(fileEntity.getStoredFileName())
            .fileUrl(fileEntity.getFileUrl())
            .size(fileEntity.getSize())
            .build();
    }
}
