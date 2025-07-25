package com.devlog.repository;

import com.devlog.domain.post.Post;
import com.devlog.dto.post.PostWithThumbnailProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author sbsong
 * @package com.devlog.repository
 * @Classname PostRepository.java
 * @Description ""
 * <PRE>
 * ---------------------------------
 * 개정이력
 * 2025-07-11 sbsong : 최초작성
 * </PRE>
 * @since 2025-07-11
 */
@Repository
public interface PostRepository extends JpaRepository<Post,Long> {
    Page<Post> findAllByIsDeletedFalseOrderByCreatedAtDesc(Pageable pageable);
    Optional<Post> findByIdAndIsDeletedFalse(Long id);

    @Query(value = """
        SELECT p.id AS id,
               p.title AS title,
               p.content AS content,
               f.filePath AS filePath
        FROM Post p
        LEFT JOIN File f ON f.post.id = p.id
        WHERE p.isDeleted = false
    """,
        countQuery = """
                    SELECT count(p)
                    FROM Post p
                    WHERE p.isDeleted = false
                """)
    Page<PostWithThumbnailProjection> findAllWithThumbnail(Pageable pageable);
}
