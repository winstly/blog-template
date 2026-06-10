package cn.yanshisan.blog.content.infrastructure.ohs;

import cn.yanshisan.blog.content.domain.entity.Article;
import cn.yanshisan.blog.content.domain.repository.ArticleRepository;
import cn.yanshisan.blog.shared.ohs.ArticleOHS;
import cn.yanshisan.blog.shared.ohs.ArticleSummaryVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ArticleOHSImpl implements ArticleOHS {

    private final ArticleRepository articleRepository;

    @Override
    public String getArticleTitle(String articleCode) {
        return articleRepository.findByCode(articleCode)
                .map(Article::getTitle)
                .orElse(null);
    }

    @Override
    public ArticleSummaryVO getArticleSummary(String articleCode) {
        return articleRepository.findByCode(articleCode)
                .map(article -> new ArticleSummaryVO(
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
