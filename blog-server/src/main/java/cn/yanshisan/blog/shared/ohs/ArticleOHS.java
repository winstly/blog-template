package cn.yanshisan.blog.shared.ohs;

public interface ArticleOHS {

    String getArticleTitle(String articleCode);

    ArticleSummaryVO getArticleSummary(String articleCode);

    boolean existsArticle(String articleCode);
}
