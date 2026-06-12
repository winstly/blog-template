package com.winstly.blog.interaction.domain.factory;

import com.winstly.blog.interaction.domain.entity.InteractionDO;
import com.winstly.blog.interaction.domain.enums.Action;
import com.winstly.blog.interaction.domain.enums.DisplayStatus;
import com.winstly.blog.interaction.domain.enums.TreeConstants;
import com.winstly.blog.interaction.domain.valueobject.Target;
import org.springframework.stereotype.Component;

@Component
public class InteractionFactory {

    public InteractionDO create(Target target, Action action, String userName,
                              String userAvatarUrl, String remark, String extMeta) {
        InteractionDO interaction = new InteractionDO();
        interaction.setTargetType(target.getTargetType());
        interaction.setTargetCode(target.getTargetCode());
        interaction.setActionType(action);
        interaction.setUserName(userName);
        interaction.setUserAvatarUrl(userAvatarUrl);
        interaction.setRemark(remark);
        interaction.setTreeDepth(TreeConstants.ROOT_DEPTH);
        interaction.setExtMeta(extMeta);
        interaction.setDisplayStatus(DisplayStatus.APPROVED.getValue());
        return interaction;
    }

    public InteractionDO createReply(InteractionDO parent, Action action, String userName,
                                   String userAvatarUrl, String remark, String extMeta) {
        InteractionDO reply = new InteractionDO();
        reply.setTargetType(parent.getTargetType());
        reply.setTargetCode(parent.getTargetCode());
        reply.setActionType(action);
        reply.setUserName(userName);
        reply.setUserAvatarUrl(userAvatarUrl);
        reply.setRemark(remark);
        reply.setTreeDepth(parent.getTreeDepth() + 1);
        reply.setExtMeta(extMeta);
        reply.setDisplayStatus(DisplayStatus.APPROVED.getValue());
        return reply;
    }
}
