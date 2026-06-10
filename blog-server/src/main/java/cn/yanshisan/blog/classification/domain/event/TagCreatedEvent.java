package cn.yanshisan.blog.classification.domain.event;

import org.springframework.context.ApplicationEvent;

public class TagCreatedEvent extends ApplicationEvent {

    private final String tagCode;
    private final String tagName;

    public TagCreatedEvent(Object source, String tagCode, String tagName) {
        super(source);
        this.tagCode = tagCode;
        this.tagName = tagName;
    }

    public String getTagCode() {
        return tagCode;
    }

    public String getTagName() {
        return tagName;
    }
}
