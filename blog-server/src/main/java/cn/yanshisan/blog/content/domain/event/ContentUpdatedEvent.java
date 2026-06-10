package cn.yanshisan.blog.content.domain.event;

import org.springframework.context.ApplicationEvent;

public class ContentUpdatedEvent extends ApplicationEvent {

    private final String articleCode;
    private final int version;

    public ContentUpdatedEvent(Object source, String articleCode, int version) {
        super(source);
        this.articleCode = articleCode;
        this.version = version;
    }

    public String getArticleCode() {
        return articleCode;
    }

    public int getVersion() {
        return version;
    }
}
