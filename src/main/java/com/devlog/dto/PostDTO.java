package com.devlog.dto;

import com.devlog.domain.post.Post;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

/**
 * @author sbsong
 * @package com.devlog.dto
 * @Classname PostDTO.java
 * @Description ""
 * <PRE>
 * ---------------------------------
 * 개정이력
 * 2025-07-11 sbsong : 최초작성
 * </PRE>
 * @since 2025-07-11
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostDTO {
    private Long id;
    private String title;
    private String content;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    public static PostDTO fromEntity(Post entity) {
        return PostDTO.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .content(entity.getContent())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public Post toEntity() {
        return Post.builder()
                .id(this.id)
                .title(this.title)
                .content(this.content)
                .build();
    }
}
