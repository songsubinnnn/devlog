package com.devlog.dto.file;

import lombok.*;

/**
 * @author sbsong
 * @package com.devlog.dto.file
 * @Classname FileResponse.java
 * @Description ""
 * <PRE>
 * ---------------------------------
 * 개정이력
 * 2025-07-23 sbsong : 최초작성
 * </PRE>
 * @since 2025-07-23
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileResponse { // 응답 시 실제 저장된 파일명, URL 등 클라이언트에게 필요한 정보 제공
    private Long id;
    private String storedFileName;
    private String originalFileName;
    private String fileUrl;
    private Long size;
}
