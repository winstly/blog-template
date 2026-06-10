package cn.yanshisan.blog.content.application;

import cn.yanshisan.blog.content.domain.entity.Article;
import cn.yanshisan.blog.content.domain.entity.Content;
import cn.yanshisan.blog.content.domain.entity.ContentRevision;
import cn.yanshisan.blog.content.domain.repository.ArticleRepository;
import cn.yanshisan.blog.content.domain.service.ArticleManagementService;
import cn.yanshisan.blog.content.domain.service.ArticlePublicationService;
import cn.yanshisan.blog.content.interfaces.dto.ArticleVO;
import cn.yanshisan.blog.shared.api.PageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleApplicationService {

    private final ArticleManagementService managementService;
    private final ArticlePublicationService publicationService;
    private final ArticleRepository articleRepository;

    public ArticleVO createArticle(String title, String summary, String coverUrl, String body, String contentFormat) {
        Article article = managementService.createArticle(title, summary, coverUrl, body, contentFormat);
        Content content = articleRepository.findContentByArticleCode(article.getArticleCode())
                .orElseThrow();
        return ArticleVO.from(article, content);
    }

    public ArticleVO updateMetadata(String articleCode, String title, String summary, String coverUrl) {
        managementService.updateMetadata(articleCode, title, summary, coverUrl);
        return getArticleDetail(articleCode);
    }

    public ArticleVO updateContent(String articleCode, String body) {
        managementService.updateContent(articleCode, body);
        return getArticleDetail(articleCode);
    }

    public ArticleVO publish(String articleCode) {
        publicationService.publish(articleCode);
        return getArticleDetail(articleCode);
    }

    public ArticleVO togglePin(String articleCode) {
        managementService.togglePin(articleCode);
        return getArticleDetail(articleCode);
    }

    public void deleteArticle(String articleCode) {
        managementService.deleteArticle(articleCode);
    }

    public ArticleVO getArticleDetail(String articleCode) {
        Article article = articleRepository.findByCode(articleCode)
                .orElseThrow();
        Content content = articleRepository.findContentByArticleCode(articleCode)
                .orElseThrow();
        return ArticleVO.from(article, content);
    }

    public PageResult<ArticleVO> listPublishedArticles(int page, int pageSize) {
        List<Article> articles = articleRepository.findPublished(page, pageSize);
        long total = articleRepository.countPublished();
        List<ArticleVO> vos = articles.stream()
                .map(article -> {
                    Content content = articleRepository.findContentByArticleCode(article.getArticleCode())
                            .orElse(null);
                    return ArticleVO.from(article, content);
                })
                .toList();
        return new PageResult<>(vos, total, page, pageSize);
    }

    public List<ContentRevision> listRevisions(String articleCode) {
        return articleRepository.findRevisionsByArticleCode(articleCode);
    }

    public ContentRevision getRevision(String articleCode, int version) {
        return articleRepository.findRevisionByVersion(articleCode, version)
                .orElseThrow();
    }
}
