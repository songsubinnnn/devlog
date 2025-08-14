package com.devlog.dto;

import java.time.LocalDateTime;

/**
 * @author sbsong
 * @package com.devlog.dto
 * @Classname ErrorResponse.java
 * @Description ""
 * <PRE>
 * ---------------------------------
 * 개정이력
 * 2025-08-14 sbsong : 최초작성
 * </PRE>
 * @since 2025-08-14
 */
public record ErrorResponse(
    LocalDateTime timestamp,
    int status,
    String error,
    String message) {
}
