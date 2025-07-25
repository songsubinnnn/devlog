package com.devlog.dto.post;

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
public interface PostWithThumbnailProjection {
        Long getId();
        String getTitle();
        String getContent();
        String getFilePath(); // File Entity의 필드

}
