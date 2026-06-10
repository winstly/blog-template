package cn.yanshisan.blog.content.domain.event;

import org.springframework.context.ApplicationEvent;

public class RevisionCreatedEvent extends ApplicationEvent {

    private final Long contentId;
    private final int version;

    public RevisionCreatedEvent(Object source, Long contentId, int version) {
        super(source);
        this.contentId = contentId;
        this.version = version;
    }

    public Long getContentId() {
        return contentId;
    }

    public int getVersion() {
        return version;
    }
}
