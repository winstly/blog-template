package com.winstly.blog.classification.domain.event;

import org.springframework.context.ApplicationEvent;

public class TagDeletedEvent extends ApplicationEvent {

    private final String tagCode;

    public TagDeletedEvent(Object source, String tagCode) {
        super(source);
        this.tagCode = tagCode;
    }

    public String getTagCode() {
        return tagCode;
    }
}
