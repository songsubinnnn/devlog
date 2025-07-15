package com.devlog.domain.post;

import com.devlog.domain.BaseEntity;
import com.devlog.domain.tag.Tag;
import jakarta.persistence.*;
import lombok.*;

/**
 * @author sbsong
 * @package com.devlog.domain.post
 * @Classname PostTag.java
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
public class PostTag extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id", nullable = false)
    private Tag tag;

    // 연결용 엔티티는 정적 팩토리 메서드 사용(단순하고 고정적인 엔티티) - 외부에서 new 하지 않고 클래스 자체에서 생성
    public static PostTag of(Post post, Tag tag){
        PostTag postTag = new PostTag();
        postTag.setPost(post);
        postTag.setTag(tag);
        return postTag;
    }
}

