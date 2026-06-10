package cn.yanshisan.blog.content.domain.repository;

import cn.yanshisan.blog.content.domain.entity.Content;
import cn.yanshisan.blog.content.domain.entity.ContentRevision;

import java.util.Optional;

public interface ContentRepository {

    void save(Content content);

    void update(Content content);

    Optional<Content> findByTargetCode(String targetType, String targetCode);

    void saveRevision(ContentRevision revision);

    ContentRevision findRevisionByVersion(Long contentId, int version);
}
