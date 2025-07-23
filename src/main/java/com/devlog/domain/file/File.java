package com.devlog.domain.file;

import com.devlog.domain.BaseEntity;
import com.devlog.domain.post.Post;
import jakarta.persistence.*;
import lombok.*;

/**
 * @author sbsong
 * @package com.devlog.domain.file
 * @Classname File.java
 * @Description ""
 * <PRE>
 * ---------------------------------
 * 개정이력
 * 2025-07-22 sbsong : 최초작성
 * </PRE>
 * @since 2025-07-22
 */
@Entity
@Table(name = "files")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class File extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String originalFileName;

    @Column(nullable = false)
    private String storedFileName;

    @Column(nullable = false)
    private Long size;

    @Column(nullable = false)
    private String fileUrl; // 클라이언트용

    @Column(nullable = false)
    private String filePath; // 실제 저장된 로컬 경로

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

}
