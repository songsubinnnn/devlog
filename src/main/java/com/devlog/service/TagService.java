package com.devlog.service;

import com.devlog.domain.tag.Tag;

import java.util.List;

/**
 * @author sbsong
 * @package com.devlog.service
 * @Classname TagService.java
 * @Description ""
 * <PRE>
 * ---------------------------------
 * 개정이력
 * 2025-07-15 sbsong : 최초작성
 * </PRE>
 * @since 2025-07-15
 */
public interface TagService {
    Tag findOrCreateByName(String name);
    List<Tag> findOrCreateByNames(List<String> names);
}
