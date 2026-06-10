package cn.yanshisan.blog.interaction.domain.service;

import cn.yanshisan.blog.interaction.domain.acl.ArticleTranslator;
import cn.yanshisan.blog.interaction.domain.entity.Interaction;
import cn.yanshisan.blog.interaction.domain.event.InteractionCreatedEvent;
import cn.yanshisan.blog.interaction.domain.event.ReplyPostedEvent;
import cn.yanshisan.blog.interaction.domain.factory.InteractionFactory;
import cn.yanshisan.blog.interaction.domain.repository.InteractionRepository;
import cn.yanshisan.blog.interaction.domain.vo.Action;
import cn.yanshisan.blog.interaction.domain.vo.Target;
import cn.yanshisan.blog.shared.api.ErrorCode;
import cn.yanshisan.blog.shared.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InteractionService {

    private final InteractionFactory interactionFactory;
    private final InteractionRepository interactionRepository;
    private final ArticleTranslator articleTranslator;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public Interaction createInteraction(Target target, Action action, String userName,
                                         String userAvatarUrl, String remark, String extMeta) {
        validateTarget(target);
        Interaction interaction = interactionFactory.create(target, action, userName, userAvatarUrl, remark, extMeta);

        eventPublisher.publishEvent(new InteractionCreatedEvent(this, interaction.getId(),
                target.getTargetType(), target.getTargetCode(), action.getValue()));
        return interaction;
    }

    @Transactional
    public Interaction createReply(Long parentId, Action action, String userName,
                                   String userAvatarUrl, String remark, String extMeta) {
        Interaction parent = interactionRepository.findById(parentId)
                .orElseThrow(() -> new BusinessException(ErrorCode.INTERACTION_TARGET_NOT_FOUND, "parent interaction not found"));

        Interaction reply = interactionFactory.createReply(parent, action, userName, userAvatarUrl, remark, extMeta);

        eventPublisher.publishEvent(new ReplyPostedEvent(this, reply.getId(), parentId,
                parent.getTargetType(), parent.getTargetCode()));
        return reply;
    }

    private void validateTarget(Target target) {
        if ("article".equals(target.getTargetType())) {
            if (!articleTranslator.existsArticle(target.getTargetCode())) {
                throw new BusinessException(ErrorCode.INTERACTION_TARGET_NOT_FOUND, target.getTargetCode());
            }
        }
    }
}
