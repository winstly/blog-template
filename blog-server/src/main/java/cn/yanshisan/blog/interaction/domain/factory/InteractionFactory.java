package cn.yanshisan.blog.interaction.domain.factory;

import cn.yanshisan.blog.interaction.domain.entity.Interaction;
import cn.yanshisan.blog.interaction.domain.repository.InteractionRepository;
import cn.yanshisan.blog.interaction.domain.vo.Action;
import cn.yanshisan.blog.interaction.domain.vo.Target;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InteractionFactory {

    private final InteractionRepository interactionRepository;

    public Interaction create(Target target, Action action, String userName,
                              String userAvatarUrl, String remark, String extMeta) {
        Interaction interaction = new Interaction();
        interaction.setTargetType(target.getTargetType());
        interaction.setTargetCode(target.getTargetCode());
        interaction.setActionType(action);
        interaction.setUserName(userName);
        interaction.setUserAvatarUrl(userAvatarUrl);
        interaction.setRemark(remark);
        interaction.setTreeDepth(0);
        interaction.setExtMeta(extMeta);

        interactionRepository.save(interaction);

        interaction.fillTreePath("/" + interaction.getId() + "/");
        interactionRepository.update(interaction);

        return interaction;
    }

    public Interaction createReply(Interaction parent, Action action, String userName,
                                   String userAvatarUrl, String remark, String extMeta) {
        Interaction reply = new Interaction();
        reply.setTargetType(parent.getTargetType());
        reply.setTargetCode(parent.getTargetCode());
        reply.setActionType(action);
        reply.setUserName(userName);
        reply.setUserAvatarUrl(userAvatarUrl);
        reply.setRemark(remark);
        reply.setTreeDepth(parent.getTreeDepth() + 1);
        reply.setExtMeta(extMeta);

        interactionRepository.save(reply);

        reply.fillTreePath(parent.getTreePath() + reply.getId() + "/");
        interactionRepository.update(reply);

        return reply;
    }
}
