package cn.yanshisan.blog.content.domain.repository;

import cn.yanshisan.blog.content.domain.entity.Article;
import cn.yanshisan.blog.content.domain.entity.Content;
import cn.yanshisan.blog.content.domain.entity.ContentRevision;
import cn.yanshisan.blog.content.domain.vo.PublishStatus;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository {

    void save(Article article, Content content);

    void update(Article article, Content content);

    Optional<Article> findByCode(String articleCode);

    Optional<Content> findContentByArticleCode(String articleCode);

    List<ContentRevision> findRevisionsByArticleCode(String articleCode);

    Optional<ContentRevision> findRevisionByVersion(String articleCode, int version);

    void delete(Article article, Content content);

    long countPublished();

    List<Article> findPublished(int page, int pageSize);

    boolean existsByArticleCode(String articleCode);

    void saveRevision(ContentRevision revision);
}
