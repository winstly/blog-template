package cn.yanshisan.blog.content.domain.service;

import cn.yanshisan.blog.content.domain.entity.Article;
import cn.yanshisan.blog.content.domain.entity.Content;
import cn.yanshisan.blog.content.domain.entity.ContentRevision;
import cn.yanshisan.blog.content.domain.event.ArticlePublishedEvent;
import cn.yanshisan.blog.content.domain.repository.ArticleRepository;
import cn.yanshisan.blog.content.domain.spec.PublishableSpec;
import cn.yanshisan.blog.shared.api.ErrorCode;
import cn.yanshisan.blog.shared.exception.BusinessException;
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
        Article article = articleRepository.findByCode(articleCode)
                .orElseThrow(() -> new BusinessException(ErrorCode.ARTICLE_NOT_FOUND, articleCode));

        Content content = articleRepository.findContentByArticleCode(articleCode)
                .orElseThrow(() -> new BusinessException(ErrorCode.ARTICLE_NOT_FOUND, articleCode));

        if (!PublishableSpec.isSatisfiedBy(article, content)) {
            throw new BusinessException(ErrorCode.ARTICLE_NOT_PUBLISHABLE, articleCode);
        }

        article.publish();
        content.publish();

        ContentRevision revision = content.createRevision("Published");
        articleRepository.update(article, content);
        articleRepository.saveRevision(revision);

        eventPublisher.publishEvent(new ArticlePublishedEvent(this, articleCode, article.getTitle()));
    }

    @Transactional
    public void unpublish(String articleCode) {
        Article article = articleRepository.findByCode(articleCode)
                .orElseThrow(() -> new BusinessException(ErrorCode.ARTICLE_NOT_FOUND, articleCode));
        Content content = articleRepository.findContentByArticleCode(articleCode)
                .orElseThrow(() -> new BusinessException(ErrorCode.ARTICLE_NOT_FOUND, articleCode));

        article.unpublish();
        content.unpublish();

        ContentRevision revision = content.createRevision("Unpublished");
        articleRepository.update(article, content);
        articleRepository.saveRevision(revision);
    }
}
