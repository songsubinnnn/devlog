package com.devlog.dto.post;

import com.devlog.dto.file.FileResponse;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

/**
 * @author sbsong
 * @package com.devlog.dto.post
 * @Classname PostRequest.java
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
public class PostRequest { // client -> server 보내는 요청데이터를 DTO로 받기 위함
    private Long id;
    @NotBlank(message = "제목은 필수값입니다.")
    private String title;
    @NotBlank(message = "내용은 필수값입니다.")
    private String content;
    private List<String> tags; // 클라이언트는 태그 이름만 보냄
    private FileResponse thumbnail;
    private List<FileResponse> attachments; // 첨부파일
}
