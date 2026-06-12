package com.winstly.blog.content.domain.factory;

import com.winstly.blog.content.domain.entity.ArticleDO;
import com.winstly.blog.content.domain.entity.ContentDO;
import com.winstly.blog.content.domain.valueobject.ArticleCode;
import org.springframework.stereotype.Component;

@Component
public class ArticleFactory {

    public ArticleDO createArticle(String title, String summary, String coverUrl, String body, String contentFormat) {
        ArticleCode articleCode = ArticleCode.fromTitle(title);

        ArticleDO article = new ArticleDO();
        article.setArticleCode(articleCode.getValue());
        article.setTitle(title);
        article.setSummary(summary);
        article.setCoverUrl(coverUrl);
        article.setIsPinned(false);
        article.setViewCount(0);

        ContentDO content = ContentDO.createForArticle(articleCode.getValue(), body, contentFormat);

        return article;
    }

    public ContentDO createContent(String articleCode, String body, String contentFormat) {
        return ContentDO.createForArticle(articleCode, body, contentFormat);
    }
}
