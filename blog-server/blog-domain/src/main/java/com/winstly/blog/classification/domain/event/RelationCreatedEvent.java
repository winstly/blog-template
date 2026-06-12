package com.winstly.blog.classification.domain.event;

import org.springframework.context.ApplicationEvent;

public class RelationCreatedEvent extends ApplicationEvent {

    private final String articleCode;
    private final String tagCode;
    private final String relationType;

    public RelationCreatedEvent(Object source, String articleCode, String tagCode, String relationType) {
        super(source);
        this.articleCode = articleCode;
        this.tagCode = tagCode;
        this.relationType = relationType;
    }

    public String getArticleCode() {
        return articleCode;
    }

    public String getTagCode() {
        return tagCode;
    }

    public String getRelationType() {
        return relationType;
    }
}
