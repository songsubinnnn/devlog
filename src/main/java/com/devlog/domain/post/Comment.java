package com.devlog.domain.post;

import com.devlog.domain.BaseEntity;
import com.devlog.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

/**
 * @author sbsong
 * @package com.devlog.domain.post
 * @Classname Comment.java
 * @Description ""
 * <PRE>
 * ---------------------------------
 * 개정이력
 * 2025-07-15 sbsong : 최초작성
 * </PRE>
 * @since 2025-07-15
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;
}
