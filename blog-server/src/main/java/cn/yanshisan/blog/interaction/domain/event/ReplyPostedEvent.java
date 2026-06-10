package cn.yanshisan.blog.interaction.domain.event;

import org.springframework.context.ApplicationEvent;

public class ReplyPostedEvent extends ApplicationEvent {

    private final Long replyId;
    private final Long parentInteractionId;
    private final String targetType;
    private final String targetCode;

    public ReplyPostedEvent(Object source, Long replyId,
                            Long parentInteractionId, String targetType, String targetCode) {
        super(source);
        this.replyId = replyId;
        this.parentInteractionId = parentInteractionId;
        this.targetType = targetType;
        this.targetCode = targetCode;
    }

    public Long getReplyId() { return replyId; }
    public Long getParentInteractionId() { return parentInteractionId; }
    public String getTargetType() { return targetType; }
    public String getTargetCode() { return targetCode; }
}
