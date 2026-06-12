package com.winstly.blog.content.domain.service;

import com.winstly.blog.content.domain.entity.ArticleDO;
import com.winstly.blog.content.domain.entity.ContentDO;
import com.winstly.blog.content.domain.entity.ContentRevisionDO;
import com.winstly.blog.content.domain.enums.RevisionSummary;
import com.winstly.blog.content.domain.event.ArticleCreatedEvent;
import com.winstly.blog.content.domain.event.ArticleDeletedEvent;
import com.winstly.blog.content.domain.event.ContentUpdatedEvent;
import com.winstly.blog.content.domain.factory.ArticleFactory;
import com.winstly.blog.content.domain.repository.ArticleRepository;
import com.winstly.blog.shared.api.ErrorCode;
import com.winstly.blog.shared.exception.BusinessException;
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
    public ArticleDO createArticle(String title, String summary, String coverUrl, String body, String contentFormat) {
        ArticleDO article = articleFactory.createArticle(title, summary, coverUrl, body, contentFormat);
        ContentDO content = articleFactory.createContent(article.getArticleCode(), body, contentFormat);

        articleRepository.save(article, content);

        eventPublisher.publishEvent(new ArticleCreatedEvent(this, article.getArticleCode(), article.getTitle()));
        return article;
    }

    @Transactional
    public void updateMetadata(String articleCode, String title, String summary, String coverUrl) {
        ArticleDO article = findArticleOrThrow(articleCode);
        article.updateMetadata(title, summary, coverUrl);

        ContentDO content = articleRepository.findContentByArticleCode(articleCode)
                .orElseThrow(() -> new BusinessException(ErrorCode.ARTICLE_NOT_FOUND, articleCode));
        articleRepository.update(article, content);
    }

    @Transactional
    public void updateContent(String articleCode, String body) {
        ArticleDO article = findArticleOrThrow(articleCode);
        ContentDO content = articleRepository.findContentByArticleCode(articleCode)
                .orElseThrow(() -> new BusinessException(ErrorCode.ARTICLE_NOT_FOUND, articleCode));

        content.updateBody(body);
        ContentRevisionDO revision = content.createRevision(RevisionSummary.CONTENT_UPDATED);

        articleRepository.update(article, content);
        articleRepository.saveRevision(revision);

        eventPublisher.publishEvent(new ContentUpdatedEvent(this, articleCode, content.getVersion()));
    }

    @Transactional
    public void togglePin(String articleCode) {
        ArticleDO article = findArticleOrThrow(articleCode);
        article.togglePin();

        ContentDO content = articleRepository.findContentByArticleCode(articleCode)
                .orElseThrow(() -> new BusinessException(ErrorCode.ARTICLE_NOT_FOUND, articleCode));
        articleRepository.update(article, content);
    }

    @Transactional
    public void deleteArticle(String articleCode) {
        ArticleDO article = findArticleOrThrow(articleCode);
        ContentDO content = articleRepository.findContentByArticleCode(articleCode)
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

    private ArticleDO findArticleOrThrow(String articleCode) {
        return articleRepository.findByCode(articleCode)
                .orElseThrow(() -> new BusinessException(ErrorCode.ARTICLE_NOT_FOUND, articleCode));
    }
}
