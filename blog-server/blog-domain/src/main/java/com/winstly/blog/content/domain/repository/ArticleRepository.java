package com.winstly.blog.content.domain.repository;

import com.winstly.blog.content.domain.entity.ArticleDO;
import com.winstly.blog.content.domain.entity.ContentDO;
import com.winstly.blog.content.domain.entity.ContentRevisionDO;
import com.winstly.blog.shared.api.PageResult;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository {

    void save(ArticleDO article, ContentDO content);

    void update(ArticleDO article, ContentDO content);

    Optional<ArticleDO> findByCode(String articleCode);

    Optional<ContentDO> findContentByArticleCode(String articleCode);

    List<ContentDO> findContentsByArticleCodes(List<String> articleCodes);

    List<ContentRevisionDO> findRevisionsByArticleCode(String articleCode);

    Optional<ContentRevisionDO> findRevisionByVersion(String articleCode, int version);

    void delete(ArticleDO article, ContentDO content);

    long countPublished();

    List<ArticleDO> findPublished(int page, int pageSize);

    boolean existsByArticleCode(String articleCode);

    void saveRevision(ContentRevisionDO revision);

    PageResult<ArticleDO> findByFilter(String keyword, String status, String categoryTagCode, int page, int pageSize);
}
