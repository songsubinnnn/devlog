package com.devlog.repository;

import com.devlog.domain.post.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

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
}
