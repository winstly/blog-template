package com.winstly.blog.interaction.domain.service;

import com.winstly.blog.interaction.domain.acl.ArticleTranslator;
import com.winstly.blog.interaction.domain.entity.InteractionDO;
import com.winstly.blog.interaction.domain.event.InteractionCreatedEvent;
import com.winstly.blog.interaction.domain.event.ReplyPostedEvent;
import com.winstly.blog.interaction.domain.factory.InteractionFactory;
import com.winstly.blog.interaction.domain.repository.InteractionRepository;
import com.winstly.blog.interaction.domain.enums.Action;
import com.winstly.blog.interaction.domain.enums.DisplayStatus;
import com.winstly.blog.interaction.domain.valueobject.Target;
import com.winstly.blog.shared.constants.TargetType;
import com.winstly.blog.shared.api.ErrorCode;
import com.winstly.blog.shared.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InteractionService {

    private final InteractionFactory interactionFactory;
    private final InteractionRepository interactionRepository;
    private final ArticleTranslator articleTranslator;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public InteractionDO createInteraction(Target target, Action action, String userName,
                                         String userAvatarUrl, String remark, String extMeta) {
        validateTarget(target);
        InteractionDO interaction = interactionFactory.create(target, action, userName, userAvatarUrl, remark, extMeta);

        interactionRepository.save(interaction);
        interaction.fillTreePath("/" + interaction.getId() + "/");
        interactionRepository.update(interaction);

        eventPublisher.publishEvent(new InteractionCreatedEvent(this, interaction.getId(),
                target.getTargetType(), target.getTargetCode(), action.getValue()));
        return interaction;
    }

    @Transactional
    public InteractionDO createReply(Long parentId, Action action, String userName,
                                   String userAvatarUrl, String remark, String extMeta) {
        InteractionDO parent = interactionRepository.findById(parentId)
                .orElseThrow(() -> new BusinessException(ErrorCode.INTERACTION_TARGET_NOT_FOUND, "parent interaction not found"));

        InteractionDO reply = interactionFactory.createReply(parent, action, userName, userAvatarUrl, remark, extMeta);

        interactionRepository.save(reply);
        reply.fillTreePath(parent.getTreePath() + reply.getId() + "/");
        interactionRepository.update(reply);

        eventPublisher.publishEvent(new ReplyPostedEvent(this, reply.getId(), parentId,
                parent.getTargetType(), parent.getTargetCode()));
        return reply;
    }

    private void validateTarget(Target target) {
        if (TargetType.ARTICLE.equals(target.getTargetType())) {
            if (!articleTranslator.existsArticle(target.getTargetCode())) {
                throw new BusinessException(ErrorCode.INTERACTION_TARGET_NOT_FOUND, target.getTargetCode());
            }
        }
    }

    public void approve(Long id) {
        interactionRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.INTERACTION_TARGET_NOT_FOUND, "interaction not found"));
        interactionRepository.updateDisplayStatus(id, DisplayStatus.APPROVED.getValue());
    }

    public void deleteInteraction(Long id) {
        interactionRepository.logicalDeleteById(id);
    }

    public void batchApprove(List<Long> ids) {
        interactionRepository.batchUpdateDisplayStatus(ids, DisplayStatus.APPROVED.getValue());
    }

    public void batchDelete(List<Long> ids) {
        interactionRepository.batchLogicalDelete(ids);
    }
}
