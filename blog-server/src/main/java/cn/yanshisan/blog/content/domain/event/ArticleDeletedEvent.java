package cn.yanshisan.blog.content.domain.event;

import org.springframework.context.ApplicationEvent;

public class ArticleDeletedEvent extends ApplicationEvent {

    private final String articleCode;

    public ArticleDeletedEvent(Object source, String articleCode) {
        super(source);
        this.articleCode = articleCode;
    }

    public String getArticleCode() {
        return articleCode;
    }
}
