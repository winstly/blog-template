package com.winstly.blog.interaction.infrastructure.acl;

import com.winstly.blog.interaction.domain.acl.ArticleTranslator;
import com.winstly.blog.shared.ohs.ArticleOHS;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ArticleTranslatorImpl implements ArticleTranslator {

    private final ArticleOHS articleOHS;

    @Override
    public String getArticleTitle(String articleCode) {
        return articleOHS.getArticleTitle(articleCode);
    }

    @Override
    public boolean existsArticle(String articleCode) {
        return articleOHS.existsArticle(articleCode);
    }
}
