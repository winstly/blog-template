package cn.yanshisan.blog.interaction.interfaces.api;

import cn.yanshisan.blog.interaction.application.InteractionApplicationService;
import cn.yanshisan.blog.interaction.interfaces.dto.InteractionCreateDTO;
import cn.yanshisan.blog.interaction.interfaces.dto.InteractionVO;
import cn.yanshisan.blog.shared.api.PageResult;
import cn.yanshisan.blog.shared.api.R;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/interactions")
@RequiredArgsConstructor
public class InteractionController {

    private final InteractionApplicationService interactionApplicationService;

    @PostMapping
    public R<InteractionVO> create(@RequestBody @Valid InteractionCreateDTO dto) {
        InteractionVO vo;
        if (dto.getParentId() != null) {
            vo = interactionApplicationService.createReply(
                    dto.getParentId(), dto.getActionType(),
                    dto.getUserName(), dto.getUserAvatarUrl(), dto.getRemark(), dto.getExtMeta());
        } else {
            vo = interactionApplicationService.createInteraction(
                    dto.getTargetType(), dto.getTargetCode(), dto.getActionType(),
                    dto.getUserName(), dto.getUserAvatarUrl(), dto.getRemark(), dto.getExtMeta());
        }
        return R.ok(vo);
    }

    @GetMapping
    public R<PageResult<InteractionVO>> list(@RequestParam String targetType,
                                             @RequestParam String targetCode,
                                             @RequestParam(defaultValue = "comment") String actionType,
                                             @RequestParam(defaultValue = "1") int page,
                                             @RequestParam(defaultValue = "20") int pageSize) {
        PageResult<InteractionVO> result = interactionApplicationService.listByTarget(
                targetType, targetCode, actionType, page, pageSize);
        return R.ok(result);
    }

    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) {
        interactionApplicationService.deleteInteraction(id);
        return R.ok();
    }
}
