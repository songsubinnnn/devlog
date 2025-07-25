package com.devlog.mapper;

import com.devlog.domain.post.Post;
import com.devlog.domain.user.User;
import com.devlog.dto.post.PostRequest;
import com.devlog.dto.post.PostResponse;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author sbsong
 * @package com.devlog.mapper
 * @Classname PostMapper.java
 * @Description ""
 * <PRE>
 * ---------------------------------
 * 개정이력
 * 2025-07-15 sbsong : 최초작성
 * </PRE>
 * @since 2025-07-15
 */
@Component
public class PostMapper { // postDTO <-> entity

    // request -> entity
    public Post toEntity(PostRequest postRequest, User author) {
        return Post.builder()
            .title(postRequest.getTitle())
            .content(postRequest.getContent())
            .author(author) // 로그인한 사용자(추후)
            .build();
    }

    // entity -> response
    public PostResponse toResponse(Post entity) {
        List<String> tags = Optional.ofNullable(entity.getPostTags())
            .orElse(Collections.emptyList()) // null이면 빈 리스트
            .stream() // List<엔티티> -> List<필드>
            .map(postTag -> postTag.getTag().getName())
            .collect(Collectors.toList());

        return PostResponse.builder()
            .id(entity.getId())
            .title(entity.getTitle())
            .content(entity.getContent())
            .authorNickname(entity.getAuthor().getNickname())
            .tags(tags)
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .build();
    }
}
