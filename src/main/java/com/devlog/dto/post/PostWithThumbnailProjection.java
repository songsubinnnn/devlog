package com.devlog.dto.post;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * @author sbsong
 * @package com.devlog.dto.post
 * @Classname PostWithThumbnailProjection.java
 * @Description ""
 * <PRE>
 * ---------------------------------
 * 개정이력
 * 2025-07-25 sbsong : 최초작성
 * </PRE>
 * @since 2025-07-25
 */
@Getter
@AllArgsConstructor
public class PostWithThumbnailProjection {
    private Long id;
    private String title;
    private String content;
    private String filePath;
    private String authorNickname;
    private LocalDateTime createdAt;

}
