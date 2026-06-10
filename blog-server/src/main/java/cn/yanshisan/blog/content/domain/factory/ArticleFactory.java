package cn.yanshisan.blog.content.domain.factory;

import cn.yanshisan.blog.content.domain.entity.Article;
import cn.yanshisan.blog.content.domain.entity.Content;
import cn.yanshisan.blog.content.domain.vo.ArticleCode;
import org.springframework.stereotype.Component;

@Component
public class ArticleFactory {

    public Article createArticle(String title, String summary, String coverUrl, String body, String contentFormat) {
        ArticleCode articleCode = ArticleCode.fromTitle(title);

        Article article = new Article();
        article.setArticleCode(articleCode.getValue());
        article.setTitle(title);
        article.setSummary(summary);
        article.setCoverUrl(coverUrl);
        article.setIsPinned(false);
        article.setViewCount(0);

        Content content = Content.createForArticle(articleCode.getValue(), body, contentFormat);

        return article;
    }

    public Content createContent(String articleCode, String body, String contentFormat) {
        return Content.createForArticle(articleCode, body, contentFormat);
    }
}
