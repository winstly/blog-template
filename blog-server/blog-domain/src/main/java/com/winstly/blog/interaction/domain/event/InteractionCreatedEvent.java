package com.winstly.blog.interaction.domain.event;

import org.springframework.context.ApplicationEvent;

public class InteractionCreatedEvent extends ApplicationEvent {

    private final Long interactionId;
    private final String targetType;
    private final String targetCode;
    private final String actionType;

    public InteractionCreatedEvent(Object source, Long interactionId,
                                   String targetType, String targetCode, String actionType) {
        super(source);
        this.interactionId = interactionId;
        this.targetType = targetType;
        this.targetCode = targetCode;
        this.actionType = actionType;
    }

    public Long getInteractionId() { return interactionId; }
    public String getTargetType() { return targetType; }
    public String getTargetCode() { return targetCode; }
    public String getActionType() { return actionType; }
}
