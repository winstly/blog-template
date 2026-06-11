package cn.yanshisan.blog.interaction.application;

import cn.yanshisan.blog.interaction.domain.entity.Interaction;
import cn.yanshisan.blog.interaction.domain.repository.InteractionRepository;
import cn.yanshisan.blog.interaction.domain.service.InteractionService;
import cn.yanshisan.blog.interaction.domain.vo.Action;
import cn.yanshisan.blog.interaction.domain.vo.Target;
import cn.yanshisan.blog.interaction.interfaces.dto.InteractionVO;
import cn.yanshisan.blog.shared.api.PageResult;
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
        Target target = "site".equals(targetType) ? Target.site() : Target.article(targetCode);
        Action action = Action.fromValue(actionType);
        Interaction interaction = interactionService.createInteraction(target, action, userName, userAvatarUrl, remark, extMeta);
        return InteractionVO.from(interaction);
    }

    public InteractionVO createReply(Long parentId, String actionType, String userName,
                                     String userAvatarUrl, String remark, String extMeta) {
        Action action = Action.fromValue(actionType);
        Interaction reply = interactionService.createReply(parentId, action, userName, userAvatarUrl, remark, extMeta);
        InteractionVO vo = InteractionVO.from(reply);
        Interaction parent = interactionRepository.findById(parentId).orElse(null);
        if (parent != null) {
            vo.setReplyTo(parent.getUserName());
        }
        return vo;
    }

    public PageResult<InteractionVO> listByTarget(String targetType, String targetCode,
                                                   String actionType, int page, int pageSize) {
        Action action = Action.fromValue(actionType);
        long total = interactionRepository.countRootsByTarget(targetType, targetCode, action);
        List<Interaction> roots = interactionRepository.findRootsByTarget(targetType, targetCode, action, page, pageSize);

        if (roots.isEmpty()) {
            return new PageResult<>(List.of(), total, page, pageSize);
        }

        List<Interaction> allReplies = interactionRepository.findRepliesByTarget(targetType, targetCode, action);
        List<InteractionVO> vos = InteractionVO.buildTree(roots, allReplies);

        return new PageResult<>(vos, total, page, pageSize);
    }

    public PageResult<InteractionVO> listByTargetAndStatus(String targetType, String targetCode,
                                                            String actionType, Integer displayStatus,
                                                            int page, int pageSize) {
        Action action = Action.fromValue(actionType);
        long total = interactionRepository.countByTargetAndStatus(targetType, targetCode, action, displayStatus);
        List<Interaction> roots = interactionRepository.findRootsByTargetAndStatus(targetType, targetCode, action, displayStatus, page, pageSize);

        if (roots.isEmpty()) {
            return new PageResult<>(List.of(), total, page, pageSize);
        }

        List<Interaction> allReplies = interactionRepository.findRepliesByTarget(targetType, targetCode, action);
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
        return listByTargetAndStatus("site", "site", "message", displayStatus, page, pageSize);
    }
}
