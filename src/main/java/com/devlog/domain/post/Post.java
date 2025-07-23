package com.devlog.domain.post;

import com.devlog.domain.BaseEntity;
import com.devlog.domain.file.File;
import com.devlog.domain.tag.Tag;
import com.devlog.domain.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author sbsong
 * @package com.devlog.entity
 * @Classname PostEntity.java
 * @Description ""
 * <PRE>
 * ---------------------------------
 * 개정이력
 * 2025-07-11 sbsong : 최초작성
 * </PRE>
 * @since 2025-07-11
 */
@Entity
@Table(name = "posts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class) // JPA Auditing
public class Post extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "thumbnail_file_id") // fk 컬럼명
    private File thumbnail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = true)
    private User author;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true) // 부모 저장/삭제 시 자식도 함께 처리
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostTag> postTags = new ArrayList<>();

    @Column(name = "view_count", nullable = false)
    private Integer viewCount = 0;

    // 비즈니스 메서드
    public void update(String title, String content, File thumbnail, List<Tag> tags) {
        this.title = title;
        this.content = content;
        this.thumbnail = thumbnail;
        this.updatedAt = LocalDateTime.now();
        this.viewCount = 0;
        // 기존 postTags 제거
        this.postTags.clear();

        // 새로운 태그 목록으로 PostTag 재생성
        if (tags != null) {
            for (Tag tag : tags) {
                if (tag != null) {
                    this.postTags.add(PostTag.of(this, tag));
                }
            }
        }
    }

    public void increaseViewCount() {
        this.viewCount++;
    }

}
