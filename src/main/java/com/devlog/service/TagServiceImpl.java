package com.devlog.service;

import com.devlog.domain.tag.Tag;
import com.devlog.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author sbsong
 * @package com.devlog.service
 * @Classname TagServiceImpl.java
 * @Description ""
 * <PRE>
 * ---------------------------------
 * 개정이력
 * 2025-07-15 sbsong : 최초작성
 * </PRE>
 * @since 2025-07-15
 */
@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;

    @Override
    public Tag findOrCreateByName(String name) {
       Tag tag = tagRepository.findByName(name)
                .orElseGet(()->tagRepository.save(Tag.of(name))); // name으로 찾아서 있으면 반환, 없으면 생성
       return tag;
    }

    @Override
    public List<Tag> findOrCreateByNames(List<String> names) {
        return names.stream()
                .distinct() // 중복 제거
                .map(this::findOrCreateByName)
                .collect(Collectors.toList()); // tag list -> 이미 존재하면 가져오고 없으면 생성
    }
}
