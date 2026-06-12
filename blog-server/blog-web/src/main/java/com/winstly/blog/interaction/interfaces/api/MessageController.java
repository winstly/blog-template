package com.winstly.blog.interaction.interfaces.api;

import com.winstly.blog.interaction.application.InteractionApplicationService;
import com.winstly.blog.interaction.domain.enums.Action;
import com.winstly.blog.interaction.domain.enums.DisplayStatus;
import com.winstly.blog.interaction.interfaces.request.MessageCreateRequest;
import com.winstly.blog.interaction.interfaces.vo.InteractionVO;
import com.winstly.blog.shared.constants.TargetType;
import com.winstly.blog.shared.api.PageResult;
import com.winstly.blog.shared.api.R;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/messages")
@RequiredArgsConstructor
public class MessageController {

    private final InteractionApplicationService interactionApplicationService;

    @PostMapping
    public R<InteractionVO> create(@RequestBody @Valid MessageCreateRequest dto) {
        InteractionVO vo;
        if (dto.getParentId() != null) {
            vo = interactionApplicationService.createReply(
                    dto.getParentId(), Action.MESSAGE.getValue(),
                    dto.getUserName(), dto.getUserAvatarUrl(), dto.getRemark(), dto.getExtMeta());
        } else {
            vo = interactionApplicationService.createInteraction(
                    TargetType.SITE, TargetType.SITE, Action.MESSAGE.getValue(),
                    dto.getUserName(), dto.getUserAvatarUrl(), dto.getRemark(), dto.getExtMeta());
        }
        return R.ok(vo);
    }

    @GetMapping
    public R<PageResult<InteractionVO>> list(@RequestParam(required = false) String status,
                                             @RequestParam(defaultValue = "1") int page,
                                             @RequestParam(defaultValue = "20") int pageSize) {
        Integer displayStatus = null;
        if (StringUtils.isNotBlank(status)) {
            displayStatus = DisplayStatus.APPROVED.equals(DisplayStatus.fromLabel(status))
                    ? DisplayStatus.APPROVED.getValue() : DisplayStatus.PENDING.getValue();
        }
        PageResult<InteractionVO> result = interactionApplicationService.listMessages(displayStatus, page, pageSize);
        return R.ok(result);
    }

    @PutMapping("/{id}/approve")
    public R<Void> approve(@PathVariable Long id) {
        interactionApplicationService.approve(id);
        return R.ok();
    }

    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) {
        interactionApplicationService.deleteInteraction(id);
        return R.ok();
    }

    @PutMapping("/batch-approve")
    public R<Void> batchApprove(@RequestBody List<Long> ids) {
        interactionApplicationService.batchApprove(ids);
        return R.ok();
    }

    @DeleteMapping("/batch")
    public R<Void> batchDelete(@RequestBody List<Long> ids) {
        interactionApplicationService.batchDelete(ids);
        return R.ok();
    }
}
