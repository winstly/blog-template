package com.winstly.blog.interaction.interfaces.api;

import com.winstly.blog.interaction.application.InteractionApplicationService;
import com.winstly.blog.interaction.interfaces.request.InteractionCreateRequest;
import com.winstly.blog.interaction.interfaces.vo.InteractionVO;
import com.winstly.blog.shared.api.PageResult;
import com.winstly.blog.shared.api.R;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/interactions")
@RequiredArgsConstructor
public class InteractionController {

    private final InteractionApplicationService interactionApplicationService;

    @PostMapping
    public R<InteractionVO> create(@RequestBody @Valid InteractionCreateRequest dto) {
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
