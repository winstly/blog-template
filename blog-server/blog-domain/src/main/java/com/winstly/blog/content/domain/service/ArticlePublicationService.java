package com.winstly.blog.content.domain.service;

import com.winstly.blog.content.domain.entity.ArticleDO;
import com.winstly.blog.content.domain.entity.ContentDO;
import com.winstly.blog.content.domain.entity.ContentRevisionDO;
import com.winstly.blog.content.domain.enums.RevisionSummary;
import com.winstly.blog.content.domain.event.ArticlePublishedEvent;
import com.winstly.blog.content.domain.repository.ArticleRepository;
import com.winstly.blog.content.domain.spec.PublishableSpec;
import com.winstly.blog.shared.api.ErrorCode;
import com.winstly.blog.shared.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ArticlePublicationService {

    private final ArticleRepository articleRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public void publish(String articleCode) {
        ArticleDO article = articleRepository.findByCode(articleCode)
                .orElseThrow(() -> new BusinessException(ErrorCode.ARTICLE_NOT_FOUND, articleCode));

        ContentDO content = articleRepository.findContentByArticleCode(articleCode)
                .orElseThrow(() -> new BusinessException(ErrorCode.ARTICLE_NOT_FOUND, articleCode));

        if (!PublishableSpec.isSatisfiedBy(article, content)) {
            throw new BusinessException(ErrorCode.ARTICLE_NOT_PUBLISHABLE, articleCode);
        }

        article.publish();
        content.publish();

        ContentRevisionDO revision = content.createRevision(RevisionSummary.PUBLISHED);
        articleRepository.update(article, content);
        articleRepository.saveRevision(revision);

        eventPublisher.publishEvent(new ArticlePublishedEvent(this, articleCode, article.getTitle()));
    }

    @Transactional
    public void unpublish(String articleCode) {
        ArticleDO article = articleRepository.findByCode(articleCode)
                .orElseThrow(() -> new BusinessException(ErrorCode.ARTICLE_NOT_FOUND, articleCode));
        ContentDO content = articleRepository.findContentByArticleCode(articleCode)
                .orElseThrow(() -> new BusinessException(ErrorCode.ARTICLE_NOT_FOUND, articleCode));

        article.unpublish();
        content.unpublish();

        ContentRevisionDO revision = content.createRevision(RevisionSummary.UNPUBLISHED);
        articleRepository.update(article, content);
        articleRepository.saveRevision(revision);
    }
}
