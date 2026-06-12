package com.winstly.blog.shared.ohs;

public interface ArticleOHS {

    String getArticleTitle(String articleCode);

    ArticleSummaryDTO getArticleSummary(String articleCode);

    boolean existsArticle(String articleCode);
}
