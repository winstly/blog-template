package cn.yanshisan.blog.content.domain.service;

import cn.yanshisan.blog.content.domain.entity.Article;
import cn.yanshisan.blog.content.domain.entity.Content;
import cn.yanshisan.blog.content.domain.entity.ContentRevision;
import cn.yanshisan.blog.content.domain.event.ArticleCreatedEvent;
import cn.yanshisan.blog.content.domain.event.ArticleDeletedEvent;
import cn.yanshisan.blog.content.domain.event.ContentUpdatedEvent;
import cn.yanshisan.blog.content.domain.factory.ArticleFactory;
import cn.yanshisan.blog.content.domain.repository.ArticleRepository;
import cn.yanshisan.blog.shared.api.ErrorCode;
import cn.yanshisan.blog.shared.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleManagementService {

    private final ArticleRepository articleRepository;
    private final ArticleFactory articleFactory;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public Article createArticle(String title, String summary, String coverUrl, String body, String contentFormat) {
        Article article = articleFactory.createArticle(title, summary, coverUrl, body, contentFormat);
        Content content = articleFactory.createContent(article.getArticleCode(), body, contentFormat);

        articleRepository.save(article, content);

        eventPublisher.publishEvent(new ArticleCreatedEvent(this, article.getArticleCode(), article.getTitle()));
        return article;
    }

    @Transactional
    public void updateMetadata(String articleCode, String title, String summary, String coverUrl) {
        Article article = findArticleOrThrow(articleCode);
        article.updateMetadata(title, summary, coverUrl);

        Content content = articleRepository.findContentByArticleCode(articleCode)
                .orElseThrow(() -> new BusinessException(ErrorCode.ARTICLE_NOT_FOUND, articleCode));
        articleRepository.update(article, content);
    }

    @Transactional
    public void updateContent(String articleCode, String body) {
        Article article = findArticleOrThrow(articleCode);
        Content content = articleRepository.findContentByArticleCode(articleCode)
                .orElseThrow(() -> new BusinessException(ErrorCode.ARTICLE_NOT_FOUND, articleCode));

        content.updateBody(body);
        ContentRevision revision = content.createRevision("Content updated");

        articleRepository.update(article, content);
        articleRepository.saveRevision(revision);

        eventPublisher.publishEvent(new ContentUpdatedEvent(this, articleCode, content.getVersion()));
    }

    @Transactional
    public void togglePin(String articleCode) {
        Article article = findArticleOrThrow(articleCode);
        article.togglePin();

        Content content = articleRepository.findContentByArticleCode(articleCode)
                .orElseThrow(() -> new BusinessException(ErrorCode.ARTICLE_NOT_FOUND, articleCode));
        articleRepository.update(article, content);
    }

    @Transactional
    public void deleteArticle(String articleCode) {
        Article article = findArticleOrThrow(articleCode);
        Content content = articleRepository.findContentByArticleCode(articleCode)
                .orElseThrow(() -> new BusinessException(ErrorCode.ARTICLE_NOT_FOUND, articleCode));

        articleRepository.delete(article, content);
        eventPublisher.publishEvent(new ArticleDeletedEvent(this, articleCode));
    }

    @Transactional
    public void deleteArticles(List<String> articleCodes) {
        for (String code : articleCodes) {
            deleteArticle(code);
        }
    }

    private Article findArticleOrThrow(String articleCode) {
        return articleRepository.findByCode(articleCode)
                .orElseThrow(() -> new BusinessException(ErrorCode.ARTICLE_NOT_FOUND, articleCode));
    }
}
