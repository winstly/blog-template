package cn.yanshisan.blog.interaction.domain.acl;

public interface ArticleTranslator {

    String getArticleTitle(String articleCode);

    boolean existsArticle(String articleCode);
}
