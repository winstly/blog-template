package cn.yanshisan.blog.content.domain.event;

import org.springframework.context.ApplicationEvent;

public class ArticlePublishedEvent extends ApplicationEvent {

    private final String articleCode;
    private final String title;

    public ArticlePublishedEvent(Object source, String articleCode, String title) {
        super(source);
        this.articleCode = articleCode;
        this.title = title;
    }

    public String getArticleCode() {
        return articleCode;
    }

    public String getTitle() {
        return title;
    }
}
