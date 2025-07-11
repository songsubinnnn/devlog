package com.devlog.repository;

import com.devlog.entity.PostEntity;
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
public interface PostRepository extends JpaRepository<PostEntity,Long> {

}
