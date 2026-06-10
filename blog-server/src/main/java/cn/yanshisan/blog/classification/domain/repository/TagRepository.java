package cn.yanshisan.blog.classification.domain.repository;

import cn.yanshisan.blog.classification.domain.entity.Tag;

import java.util.List;
import java.util.Optional;

public interface TagRepository {

    void save(Tag tag);

    void update(Tag tag);

    Optional<Tag> findByCode(String tagCode);

    List<Tag> findAll();

    List<Tag> findByTreePathPrefix(String treePathPrefix);

    void delete(Tag tag);

    boolean existsByTagCode(String tagCode);

    Optional<Tag> findById(Long id);
}
