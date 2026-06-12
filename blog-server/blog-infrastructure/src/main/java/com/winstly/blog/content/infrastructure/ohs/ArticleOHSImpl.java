package com.winstly.blog.content.infrastructure.ohs;

import com.winstly.blog.content.domain.entity.ArticleDO;
import com.winstly.blog.content.domain.repository.ArticleRepository;
import com.winstly.blog.shared.ohs.ArticleOHS;
import com.winstly.blog.shared.ohs.ArticleSummaryDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ArticleOHSImpl implements ArticleOHS {

    private final ArticleRepository articleRepository;

    @Override
    public String getArticleTitle(String articleCode) {
        return articleRepository.findByCode(articleCode)
                .map(ArticleDO::getTitle)
                .orElse(null);
    }

    @Override
    public ArticleSummaryDTO getArticleSummary(String articleCode) {
        return articleRepository.findByCode(articleCode)
                .map(article -> new ArticleSummaryDTO(
                        article.getArticleCode(),
                        article.getTitle(),
                        article.getSummary(),
                        article.getCoverUrl(),
                        article.getPublishedAt()
                ))
                .orElse(null);
    }

    @Override
    public boolean existsArticle(String articleCode) {
        return articleRepository.existsByArticleCode(articleCode);
    }
}
