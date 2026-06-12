package com.winstly.blog.classification.domain.repository;

import com.winstly.blog.classification.domain.entity.TagDO;

import java.util.List;
import java.util.Optional;

public interface TagRepository {

    void save(TagDO tag);

    void update(TagDO tag);

    Optional<TagDO> findByCode(String tagCode);

    List<TagDO> findAll();

    List<TagDO> findByTreePathPrefix(String treePathPrefix);

    void delete(TagDO tag);

    boolean existsByTagCode(String tagCode);

    Optional<TagDO> findById(Long id);
}
