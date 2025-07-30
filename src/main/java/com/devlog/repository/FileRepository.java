package com.devlog.repository;

import com.devlog.domain.file.File;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author sbsong
 * @package com.devlog.repository
 * @Classname FileRepository.java
 * @Description ""
 * <PRE>
 * ---------------------------------
 * 개정이력
 * 2025-07-23 sbsong : 최초작성
 * </PRE>
 * @since 2025-07-23
 */
public interface FileRepository extends JpaRepository<File,Long> {
    List<File> findByPostIdAndIsDeletedFalse(Long id);
}
