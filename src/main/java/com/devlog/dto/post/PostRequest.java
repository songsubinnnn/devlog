package com.devlog.dto.post;

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
public class PostRequest { // client -> server
    private Long id;
    @NotBlank(message = "제목은 필수값입니다.")
    private String title;
    @NotBlank(message = "내용은 필수값입니다.")
    private String content;
    private String thumbnailUrl;
    private List<String> tags; // 클라이언트는 태그 이름만 보냄
}
