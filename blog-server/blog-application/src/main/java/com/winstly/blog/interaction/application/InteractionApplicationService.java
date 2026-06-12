package com.winstly.blog.interaction.application;

import com.winstly.blog.interaction.domain.entity.InteractionDO;
import com.winstly.blog.interaction.domain.repository.InteractionRepository;
import com.winstly.blog.interaction.domain.service.InteractionService;
import com.winstly.blog.interaction.domain.enums.Action;
import com.winstly.blog.interaction.domain.enums.DisplayStatus;
import com.winstly.blog.interaction.domain.valueobject.Target;
import com.winstly.blog.shared.constants.TargetType;
import com.winstly.blog.interaction.interfaces.vo.InteractionVO;
import com.winstly.blog.shared.api.PageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InteractionApplicationService {

    private final InteractionService interactionService;
    private final InteractionRepository interactionRepository;

    public InteractionVO createInteraction(String targetType, String targetCode, String actionType,
                                           String userName, String userAvatarUrl, String remark, String extMeta) {
        Target target = TargetType.SITE.equals(targetType) ? Target.site() : Target.article(targetCode);
        Action action = Action.fromValue(actionType);
        InteractionDO interaction = interactionService.createInteraction(target, action, userName, userAvatarUrl, remark, extMeta);
        return InteractionVO.from(interaction);
    }

    public InteractionVO createReply(Long parentId, String actionType, String userName,
                                     String userAvatarUrl, String remark, String extMeta) {
        Action action = Action.fromValue(actionType);
        InteractionDO reply = interactionService.createReply(parentId, action, userName, userAvatarUrl, remark, extMeta);
        InteractionVO vo = InteractionVO.from(reply);
        InteractionDO parent = interactionRepository.findById(parentId).orElse(null);
        if (parent != null) {
            vo.setReplyTo(parent.getUserName());
        }
        return vo;
    }

    public PageResult<InteractionVO> listByTarget(String targetType, String targetCode,
                                                   String actionType, int page, int pageSize) {
        Action action = Action.fromValue(actionType);
        long total = interactionRepository.countRootsByTarget(targetType, targetCode, action);
        List<InteractionDO> roots = interactionRepository.findRootsByTarget(targetType, targetCode, action, page, pageSize);

        if (roots.isEmpty()) {
            return new PageResult<>(List.of(), total, page, pageSize);
        }

        List<InteractionDO> allReplies = interactionRepository.findRepliesByTarget(targetType, targetCode, action);
        List<InteractionVO> vos = InteractionVO.buildTree(roots, allReplies);

        return new PageResult<>(vos, total, page, pageSize);
    }

    public PageResult<InteractionVO> listByTargetAndStatus(String targetType, String targetCode,
                                                            String actionType, Integer displayStatus,
                                                            int page, int pageSize) {
        Action action = Action.fromValue(actionType);
        long total = interactionRepository.countByTargetAndStatus(targetType, targetCode, action, displayStatus);
        List<InteractionDO> roots = interactionRepository.findRootsByTargetAndStatus(targetType, targetCode, action, displayStatus, page, pageSize);

        if (roots.isEmpty()) {
            return new PageResult<>(List.of(), total, page, pageSize);
        }

        List<InteractionDO> allReplies = interactionRepository.findRepliesByTarget(targetType, targetCode, action);
        List<InteractionVO> vos = InteractionVO.buildTree(roots, allReplies);

        return new PageResult<>(vos, total, page, pageSize);
    }

    public void approve(Long id) {
        interactionService.approve(id);
    }

    public void deleteInteraction(Long id) {
        interactionService.deleteInteraction(id);
    }

    public void batchApprove(List<Long> ids) {
        interactionService.batchApprove(ids);
    }

    public void batchDelete(List<Long> ids) {
        interactionService.batchDelete(ids);
    }

    public PageResult<InteractionVO> listMessages(Integer displayStatus, int page, int pageSize) {
        return listByTargetAndStatus(TargetType.SITE, TargetType.SITE, Action.MESSAGE.getValue(), displayStatus, page, pageSize);
    }
}
