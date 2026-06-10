package cn.yanshisan.blog.classification.domain.event;

import org.springframework.context.ApplicationEvent;

public class TagMovedEvent extends ApplicationEvent {

    private final String tagCode;
    private final String oldTreePath;
    private final String newTreePath;

    public TagMovedEvent(Object source, String tagCode, String oldTreePath, String newTreePath) {
        super(source);
        this.tagCode = tagCode;
        this.oldTreePath = oldTreePath;
        this.newTreePath = newTreePath;
    }

    public String getTagCode() {
        return tagCode;
    }

    public String getOldTreePath() {
        return oldTreePath;
    }

    public String getNewTreePath() {
        return newTreePath;
    }
}
