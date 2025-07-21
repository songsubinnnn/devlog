package com.devlog;

import com.devlog.domain.post.Post;
import com.devlog.domain.tag.Tag;
import com.devlog.repository.PostRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ExtendWith(SpringExtension.class)
class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private EntityManager em;

    @Test
    @DisplayName("태그 없이 게시글 저장이 가능해야 한다")
    void savePostWithoutTags() {
        Post post = new Post();
        post.setTitle("태그 없음");
        post.setContent("태그 없이도 저장됩니다.");
        post.setThumbnailUrl(null);

        postRepository.save(post);

        em.flush();
        em.clear();

        Post saved = postRepository.findById(post.getId())
            .orElseThrow(() -> new RuntimeException("게시글 없음"));

        assertThat(saved.getPostTags()).isNotNull();  // null 아님
        assertThat(saved.getPostTags()).isEmpty();    // 빈 리스트
    }

    @Test
    @DisplayName("null 대신 빈 태그 리스트로 게시글 저장이 가능해야 한다")
    void savePostWithEmptyTagList() {
        Post post = new Post();
        post.setTitle("빈 태그 리스트");
        post.setContent("빈 리스트도 허용");
        post.setThumbnailUrl(null);

        post.update(post.getTitle(), post.getContent(), post.getThumbnailUrl(), Collections.emptyList());

        postRepository.save(post);

        em.flush();
        em.clear();

        Post saved = postRepository.findById(post.getId())
            .orElseThrow(() -> new RuntimeException("게시글 없음"));

        assertThat(saved.getPostTags()).isNotNull();
        assertThat(saved.getPostTags()).isEmpty();
    }

    @Test
    @DisplayName("태그가 있는 게시글은 PostTag가 생성되어야 한다")
    void savePostWithTags() {
        Tag tag1 = new Tag("Java");
        Tag tag2 = new Tag("Spring");

        Post post = new Post();
        post.setTitle("태그 있음");
        post.setContent("태그 있음 테스트");
        post.setThumbnailUrl(null);
        post.update(post.getTitle(), post.getContent(), post.getThumbnailUrl(), List.of(tag1, tag2));

        postRepository.save(post);

        em.flush();
        em.clear();

        Post saved = postRepository.findById(post.getId())
            .orElseThrow(() -> new RuntimeException("게시글 없음"));

        assertThat(saved.getPostTags()).hasSize(2);
        assertThat(saved.getPostTags())
            .extracting(pt -> pt.getTag().getName())
            .containsExactlyInAnyOrder("Java", "Spring");
    }
}
