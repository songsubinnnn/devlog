package com.devlog.dto.post;

import com.devlog.dto.file.FileResponse;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author sbsong
 * @package com.devlog.dto.post
 * @Classname PostResponse.java
 * @Description ""
 * <PRE>
 * ---------------------------------
 * 개정이력
 * 2025-07-15 sbsong : 최초작성
 * </PRE>
 * @since 2025-07-15
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostResponse {
    private Long id;
    private String title;
    private String content;
    private String authorNickname; // response에만 필요한 데이터
    private List<String> tags; // List<PostTag> -> List<String>

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

//    @JsonIgnore
//    private String thumbnailPath; // 저장 경로
//    private String thumbnailBase64; // base64 인코딩된 썸네일
    private FileResponse thumbnail;
    private List<FileResponse> attachments; // 첨부파일


    // 썸네일이 있는지 확인하는 메서드
    public boolean hasThumbnail() {
        return thumbnail != null;
    }
}
