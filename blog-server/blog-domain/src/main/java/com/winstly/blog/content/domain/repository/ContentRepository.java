package com.winstly.blog.content.domain.repository;

import com.winstly.blog.content.domain.entity.ContentDO;
import com.winstly.blog.content.domain.entity.ContentRevisionDO;

import java.util.Optional;

public interface ContentRepository {

    void save(ContentDO content);

    void update(ContentDO content);

    Optional<ContentDO> findByTargetCode(String targetType, String targetCode);

    void saveRevision(ContentRevisionDO revision);

    ContentRevisionDO findRevisionByVersion(Long contentId, int version);
}
