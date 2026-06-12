package com.winstly.blog.content.domain.event;

import org.springframework.context.ApplicationEvent;

public class ArticleCreatedEvent extends ApplicationEvent {

    private final String articleCode;
    private final String title;

    public ArticleCreatedEvent(Object source, String articleCode, String title) {
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
